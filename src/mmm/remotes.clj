(ns mmm.remotes
  (:use [shoreleave.middleware.rpc :refer (defremote)])
  (:require [mmm.model.movie :as movie]))


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