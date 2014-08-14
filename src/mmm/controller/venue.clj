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

(defn edit [id]
  (layout/common (view/edit (model/getByID id))))

(defn update [id venue-map]
  (model/update id venue-map)
  (ring/redirect (str "/venues/" id)))

(defroutes routes
  (GET ["/venues/:id" :id #"[0-9a-f]+"] [id] (render-request view id))
  (GET ["/venues/edit/:id" :id #"[0-9a-f]+"] [id] (render-request edit id))
  (POST ["/venues/update/:id" :id #"[0-9a-f]+"] [id & params] (update id params)))
