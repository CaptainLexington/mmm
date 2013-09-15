(ns mmm.controller.movies
  (:use [compojure.core :only (defroutes GET POST)]
        [mmm.utils])
  (:require [clojure.string :as str]
            [ring.util.response :as ring]
            [mmm.view.movies :as view]
            [mmm.model.movie :as model]))

