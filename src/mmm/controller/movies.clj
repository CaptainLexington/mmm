(ns mmm.controller.movies
  (:use [compojure.core :only (defroutes GET POST)])
  (:require [clojure.string :as str]
            [ring.util.response :as ring]
            [mmm.view.movies :as view]
            [mmm.model.movie :as model]))


(defn index []
  (view/index (model/all)))

(defn addMovieForm []
  (view/add))

(defn addMovie [title director year runningTime releaseYear mpaa poster description]
  (model/add title director year runningTime releaseYear mpaa poster description))

(defroutes routes
  (GET "/" [] (index))
  (GET "/movies" [] (index))
  (GET "/movie/add" [] (addMovieForm))
  (POST "/movie/add" [title director year runningTime releaseYear mpaa poster description] (addMovie title director year runningTime releaseYear mpaa poster description))
  )
