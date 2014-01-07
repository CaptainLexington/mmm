(ns mmm.screening
  (:require
   [enfocus.core :as ef]
   [enfocus.events :as events]
   [enfocus.effects :as effects]
   [shoreleave.remotes.http-rpc :as rpc]
   [mmm.views :as views])
  (:require-macros [enfocus.macros :as em]
                   [shoreleave.remotes.macros :as slm]))



(defn addAnItem [mouseEvent remote]
  (let [submit (.-target mouseEvent)
        movieForm (.-parentElement (.-parentElement submit))
        formData (ef/from movieForm (ef/read-form))]
    (do
      (.preventDefault mouseEvent)
      (rpc/remote-callback
       remote [formData]
       #(ef/at
         [:div.add.inner-form]
         (ef/remove-node))
       ))))

(defn addMovie [mouseEvent]
  (addAnItem mouseEvent :addMovie))

(defn addPresenter [mouseEvent]
  (addAnItem mouseEvent :addPresenter))

(defn addVenue [mouseEvent]
  (addAnItem mouseEvent :addVenue))

(defn removeSelect [mouseEvent]
  (let [div (.-parentElement (.-target mouseEvent))]
    (ef/at
     div
     (ef/remove-node)))
  )

(em/defaction setup-remotes []
              [:button.add-movie] (events/listen :click addMovie)
              [:button.add-presenter] (events/listen :click addPresenter)
              [:button.add-venue] (events/listen :click addVenue))

(em/defaction setup-selects []
              [:span.remove] (events/listen :click removeSelect))


(defn duplicateFormInput [mouseEvent formInput]
  (let [plus (.-target mouseEvent)
        item (.-parentElement plus)]
    (do
      (ef/at item (ef/append
                   formInput
                   ))
      (setup-selects))))

(defn duplicateSelect [mouseEvent remote inputSnippet]
  (rpc/remote-callback
   remote
   []
   #(duplicateFormInput
     mouseEvent
     (views/itemSelect % inputSnippet))))

(defn duplicateMovieSelect [mouseEvent]
  (duplicateSelect mouseEvent :allMovies views/movieSelect))

(defn duplicatePresenterSelect [mouseEvent]
  (duplicateSelect mouseEvent :allPresenters views/presenterSelect))


(defn loadAddForm [selector form]
  (do
    (ef/at [selector]
           (ef/content (form)))
    (setup-remotes)))

(defn loadAddMovieForm []
  (loadAddForm :div.add-movie views/addMovieForm))

(defn loadAddPresenterForm []
  (loadAddForm :div.add-presenter views/addPresenterForm))

(defn loadAddVenueForm []
  (loadAddForm :div.add-venue views/addVenueForm))

(em/defaction setup []
              [:div.film :span.duplicate] (events/listen :click duplicateMovieSelect)
              [:div.presenters :span.duplicate] (events/listen :click duplicatePresenterSelect)
              [:div.showtimes :span.duplicate] (events/listen :click #(duplicateFormInput % views/showtimesInput))
              [:span.add.movie] (events/listen :click loadAddMovieForm)
              [:span.add.presenter] (events/listen :click loadAddPresenterForm)
              [:span.add.venue] (events/listen :click loadAddVenueForm)
              )