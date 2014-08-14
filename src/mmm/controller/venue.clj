(ns mmm.controller.venue
  (:use [compojure.core :only (defroutes GET POST)]
        [mmm.utils])
  (:require [clojure.string :as str]
            [ring.util.response :as ring]
            [ring.middleware [multipart-params :as mp]]
            [mmm.files :as files]
            [mmm.view.layout :as layout]
            [mmm.view.venues :as view]
            [mmm.model.venue :as model]))


(defn view [id]
  (layout/common (view/view (model/getByID id))))

(defroutes routes
  (GET ["/venues/:id" :id #"[0-9a-f]+"] [id] (render-request view id)))
