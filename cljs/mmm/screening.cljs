(ns mmm.screening
  (:require
   [enfocus.core :as ef]
   [enfocus.events :as events]
   [enfocus.effects :as effects]
   [ajax.core :refer [GET POST]]
   [mmm.views :as views]
   [mmm.moviedb :as moviedb])
  (:require-macros [enfocus.macros :as em]))



(defn addAnItem [mouseEvent URL callback]
  (let [submit (.-target mouseEvent)
        form (.-parentElement (.-parentElement submit))
        formData (ef/from form (ef/read-form))]
    (do
      (.preventDefault mouseEvent)
      (POST
        URL
       {:params formData
        :format :raw
        :handler #(do
                    (ef/at
                      [:div.add.inner-form]
                      (ef/remove-node))
                    (if (not (nil? callback))
                      (callback)))}))))

(defn addMovie [mouseEvent]
  (addAnItem 
    mouseEvent 
    "/movies/add"
    nil))

(defn addPresenter [mouseEvent]
  (addAnItem
    mouseEvent
    "/presenters/add" 
    nil))

(defn addVenue [mouseEvent]
  (addAnItem mouseEvent
             "/venues/add"
             #(refreshSelect 
                :div.venue.selectsContainer 
                "/venues/all" 
                views/venueSelect 
                "venue")))

(defn addSeries [mouseEvent]
  (addAnItem mouseEvent
             "/series/add"
             #(refreshSelect 
                :div.series.selectsContainer 
                "/series/all"
                views/seriesSelect 
                "series")))

(defn removeSelect [mouseEvent]
  (let [div (.-parentElement (.-target mouseEvent))]
    (ef/at
     div
     (ef/remove-node))))



(defn fill-in-movie-data [movie]
  (ef/at
    [:div.film :input.title]
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

(defn refreshSelect [selector URL inputSnippet value]
  (POST
   URL
   {:handler  #(do (ef/at [selector] 
                          (ef/content
                            (views/itemSelect % inputSnippet value)))
                   (setup-selects))
    :response-format :json
    :keywords? true}))

(defn duplicateFormInput [mouseEvent formInput]
  (let [plus (.-target mouseEvent)
        item (.-parentElement plus)]
    (do
      (ef/at item (ef/append
                   formInput))
      (setup-selects))))

(defn duplicateSelect [mouseEvent URL inputSnippet value]
  (POST
   URL
   {:handler  #(duplicateFormInput
                 mouseEvent
                 (views/itemSelect % inputSnippet value))
    :response-format :json
    :keywords? true}))

(defn duplicateMovieSelect [mouseEvent]
  (duplicateSelect
    mouseEvent
    "/movies/all"
    views/movieSelect 
    "movie"))

(defn duplicatePresenterSelect [mouseEvent]
  (duplicateSelect
    mouseEvent
    "/presenters/all"
    views/presenterSelect
    "presenter"))


(defn loadAddForm [selector form]
  (do
    (ef/at [selector]
           (ef/content (form)))
    (setup-remotes)
    (setup-selects)))

(defn loadAddMovieForm []
  (loadAddForm 
    :div.add-movie 
    views/addMovieForm))

(defn loadAddPresenterForm []
  (loadAddForm 
    :div.add-presenter
    views/addPresenterForm))

(defn loadAddVenueForm []
  (loadAddForm 
    :div.add-venue 
    views/addVenueForm))

(defn loadAddSeriesForm []
  (loadAddForm 
    :div.add-series 
    views/addSeriesForm))


(em/defaction setup []
              [:div.film :span.duplicate] (events/listen :click duplicateMovieSelect)
              [:div.presenters :span.duplicate] (events/listen :click duplicatePresenterSelect)
              [:div.showtimes :span.duplicate] (events/listen :click #(duplicateFormInput % (views/showtimesInput)))
              [:span.add.movie] (events/listen :click loadAddMovieForm)
              [:span.add.presenter] (events/listen :click loadAddPresenterForm)
              [:span.add.venue] (events/listen :click loadAddVenueForm)
              [:span.add.series] (events/listen :click loadAddSeriesForm))
