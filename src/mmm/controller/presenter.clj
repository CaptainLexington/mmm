(ns mmm.controller.presenter
  (:use [compojure.core :only (defroutes GET POST)]
        [mmm.utils])
  (:require [clojure.string :as str]
            [cheshire.core :as cheshire]
            [ring.util.response :as ring]
            [ring.middleware [multipart-params :as mp]]
            [cemerick.friend :as friend]
            [mmm.files :as files]
            [mmm.view.layout :as layout]
            [mmm.view.presenters :as view]
            [mmm.model.presenter :as model]))

(defn all [presenters]
  (layout/common (view/all presenters)))

(defn view [id]
  (let [presenter (model/getByID id)]
    (layout/common (view/view presenter) (str "Coming Soon from " (:name presenter) "!"))))
(defn edit [id]
  (layout/common (view/edit (model/getByID id))))

(defn update [id venue-map]
  (model/update id venue-map)
  (ring/redirect (str "/presenters/" id)))

(defn delete [id]
  (model/delete id)
  (ring/redirect "/presenters/all"))



(defroutes routes
  (GET ["/presenters/:id" :id #"[0-9a-f]+"] [id] (render-request view id))
  (GET "/presenters/all" [] (friend/authorize #{"admin"}) (render-request all (model/all)))
  (POST "/presenters/add" [& params]  (model/add params))
  (POST "/presenters/all" []  (cheshire/generate-string (model/all)))
  (GET ["/presenters/edit/:id" :id #"[0-9a-f]+"] [id] (friend/authorize #{"admin"}) (render-request edit id))
  (POST ["/presenters/update/:id" :id #"[0-9a-f]+"] [id & params] (update id params))
  (GET ["/presenters/delete/:id" :id #"[0-9a-f]+"] [id] (friend/authorize #{"admin"}) (delete id)))
