(ns mmm.controller.movie
  (:use [compojure.core :only (defroutes GET POST)]
        [mmm.utils])
  (:require [clojure.string :as str]
            [ring.util.response :as ring]
            [ring.middleware [multipart-params :as mp]]
            [mmm.files :as files]
            [mmm.view.movies :as view]
            [mmm.model.movie :as model]))


(defn add-poster [params]
  (files/add-poster params))


(defroutes routes
  (mp/wrap-multipart-params
   (POST "/movies/add" [& params] (add-poster params))))



