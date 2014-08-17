(ns mmm.controller.venue
  (:use [compojure.core :only (defroutes GET POST)]
        [mmm.utils])
  (:require [clojure.string :as str]
            [ring.util.response :as ring]
            [ring.middleware [multipart-params :as mp]]
            [cemerick.friend :as friend]
            [mmm.files :as files]
            [mmm.view.layout :as layout]
            [mmm.view.venues :as view]
            [mmm.model.venue :as model]))

(defn all [venues]
  (layout/common (view/all venues)))


(defn view [id]
  (layout/common (view/view (model/getByID id))))

(defn edit [id]
  (layout/common (view/edit (model/getByID id))))

(defn update [id venue-map]
  (model/update id venue-map)
  (ring/redirect (str "/venues/" id)))

(defn delete [id]
  (model/delete id)
  (ring/redirect "/venues/all"))

(defroutes routes
  (GET ["/venues/:id" :id #"[0-9a-f]+"] [id] (render-request view id))
  (GET "/venues/all" [] (friend/authorize #{"admin"}) (render-request all (model/all)))
  (GET ["/venues/edit/:id" :id #"[0-9a-f]+"] [id] (friend/authorize #{"admin"}) (render-request edit id))
  (POST ["/venues/update/:id" :id #"[0-9a-f]+"] [id & params] (update id params))
  (GET ["/venues/delete/:id" :id #"[0-9a-f]+"] [id] (friend/authorize #{"admin"}) (delete id)))
