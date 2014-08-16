(ns mmm.remotes
  (:use [shoreleave.middleware.rpc :refer (defremote)])
  (:require [mmm.model.movie :as movie]
            [mmm.model.presenter :as presenter]
            [mmm.model.venue :as venue]
            [mmm.model.series :as series]))

;;MOVIES
(defremote addMovie [parameters]
  (movie/add

   parameters)

;;    (:title parameters)
;;    (:director parameters)
;;    (read-string (:runningTime parameters))
;;    (read-string (:releaseYear parameters))
;;    (:mpaa parameters)
;;    (:poster parameters)
;;    (:description parameters))
  nil)

(defremote allMovies []
  (movie/all))

;;PRESENTERS
(defremote addPresenter [parameters]
  (presenter/add
   (:name parameters)
   (:website parameters)
   (:description parameters))
  nil)

(defremote allPresenters []
  (presenter/all))

;;SERIES
(defremote addSeries [parameters]
  (series/add
   (:name parameters)
   (:website parameters)
   (:description parameters))
  nil)

(defremote allSeries []
  (into [{:name "None"}] (series/all)))

;;VENUES
(defremote addVenue [venue-map]
  (venue/add venue-map)
  nil)

(defremote allVenues []
  (venue/all))
