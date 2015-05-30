(ns mmm.screening
  (:require
   [enfocus.core :as ef]
   [enfocus.events :as events]
   [enfocus.effects :as effects]
   [shoreleave.remotes.http-rpc :as rpc]
   [mmm.views :as views]
   [mmm.moviedb :as moviedb])
  (:require-macros [enfocus.macros :as em]
                   [shoreleave.remotes.macros :as slm]))



(defn addAnItem [mouseEvent remote callback]
  (let [submit (.-target mouseEvent)
        form (.-parentElement (.-parentElement submit))
        formData (ef/from form (ef/read-form))]
    (do
      (.preventDefault mouseEvent)
      (rpc/remote-callback
       remote [formData]
       #(do
          (ef/at
           [:div.add.inner-form]
           (ef/remove-node))
          (if (not (nil? callback))
            (callback)))
       ))
    ))


(defn addMovie [mouseEvent]
  (addAnItem mouseEvent :addMovie nil))

(defn addPresenter [mouseEvent]
  (addAnItem mouseEvent :addPresenter nil))

(defn addVenue [mouseEvent]
  (addAnItem mouseEvent
             :addVenue
             #(refreshSelect :div.venue.selectsContainer :allVenues views/venueSelect "venue")))

(defn addSeries [mouseEvent]
  (addAnItem mouseEvent
             :addSeries
             #(refreshSelect :div.series.selectsContainer :allSeries views/seriesSelect "series")))

(defn removeSelect [mouseEvent]
  (let [div (.-parentElement (.-target mouseEvent))]
    (ef/at
     div
     (ef/remove-node))))

(defn generate-tweet-text [mouse-event]
  (let [form (.-parentElement (.-parentElement (.-target mouse-event)))
        form-data (ef/from form (ef/read-form))
        movie-ids (:movie-id form-data)
        presenter-ids (:presenter-id form-data)
        venue-id (:venue-id form-data)]
    (rpc/remote-callback
      :generate-tweet-text [movie-ids presenter-ids venue-id]
      #(do
        (ef/at
          [:textarea.tweet-text]
          (ef/content %))))))


(defn fill-in-movie-data [movie]
  (ef/at
    [:input.title]
    (ef/set-attr :value (:title movie))
    [:input.director]
    (ef/set-attr :value (:director movie))
    [:input.release-year]
    (ef/set-attr :value (:release-year movie))
    [:input.running-time]
    (ef/set-attr :value (:running-time movie))
    [:input.poster]
    (ef/set-attr :value (:poster movie))
    [:textarea.description]
    (ef/content (:description movie))))


(defn query-moviedb-with-input []
  (let [id (ef/from
              [:input.moviedb] (ef/get-prop :value))]
    (moviedb/fill-in-movie fill-in-movie-data id)))

(em/defaction setup-remotes []
              [:button.moviedb] (events/listen :click query-moviedb-with-input)
              [:button.add-movie] (events/listen :click addMovie)
              [:button.add-presenter] (events/listen :click addPresenter)
              [:button.add-venue] (events/listen :click addVenue)
              [:button.add-series] (events/listen :click addSeries))

(em/defaction setup-selects []
              [:span.remove] (events/listen :click removeSelect))


(defn refreshSelect [selector remote inputSnippet value]
  (rpc/remote-callback
   remote
   []
   #(do (ef/at [selector] (ef/content
                           (views/itemSelect % inputSnippet value)
                           ))
      (setup-selects))))




(defn duplicateFormInput [mouseEvent formInput]
  (let [plus (.-target mouseEvent)
        item (.-parentElement plus)]
    (do
      (ef/at item (ef/append
                   formInput
                   ))
      (setup-selects))))

(defn duplicateSelect [mouseEvent remote inputSnippet value]
  (rpc/remote-callback
   remote
   []
   #(duplicateFormInput
     mouseEvent
     (views/itemSelect % inputSnippet value))))

(defn duplicateMovieSelect [mouseEvent]
  (duplicateSelect mouseEvent :allMovies views/movieSelect "movie"))

(defn duplicatePresenterSelect [mouseEvent]
  (duplicateSelect mouseEvent :allPresenters views/presenterSelect "presenter"))


(defn loadAddForm [selector form]
  (do
    (ef/at [selector]
           (ef/content (form)))
    (setup-remotes)
    (setup-selects)))

(defn loadAddMovieForm []
  (loadAddForm :div.add-movie views/addMovieForm))

(defn loadAddPresenterForm []
  (loadAddForm :div.add-presenter views/addPresenterForm))

(defn loadAddVenueForm []
  (loadAddForm :div.add-venue views/addVenueForm))

(defn loadAddSeriesForm []
  (loadAddForm :div.add-series views/addSeriesForm))


(em/defaction setup []
              [:div.film :span.duplicate] (events/listen :click duplicateMovieSelect)
              [:div.presenters :span.duplicate] (events/listen :click duplicatePresenterSelect)
              [:div.showtimes :span.duplicate] (events/listen :click #(duplicateFormInput % (views/showtimesInput)))
              [:span.add.movie] (events/listen :click loadAddMovieForm)
              [:span.add.presenter] (events/listen :click loadAddPresenterForm)
              [:span.add.venue] (events/listen :click loadAddVenueForm)
              [:span.add.series] (events/listen :click loadAddSeriesForm)
             ;[:button.tweet-text] (events/listen :click generate-tweet-text)
              )