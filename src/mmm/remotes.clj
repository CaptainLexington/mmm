(ns mmm.remotes
  (:use [shoreleave.middleware.rpc :refer (defremote)])
  (:require [mmm.model.movie :as movie]
            [mmm.model.presenter :as presenter]
            [mmm.model.venue :as venue]))


(defremote addMovie [parameters]
  (movie/add
   (:title parameters)
   (:director parameters)
   (read-string (:runningTime parameters))
   (read-string (:releaseYear parameters))
   (:mpaa parameters)
   (:poster parameters)
   (:description parameters)))

(defremote allMovies []
  (movie/all))

(defremote addPresenter [parameters]
  (presenter/add
   (:name parameters)
   (:website parameters)
   (:description parameters)))

(defremote allPresenters []
  (presenter/all))

(defremote addVenue [parameters]
  (venue/add
   (:name parameters)
   (:address parameters)
   (:description parameters)
   (:website parameters)
   (:phone-number parameters)))

(defremote allVenues []
  (venue/all))