(ns mmm.controller.venue
  (:use [compojure.core :only (defroutes GET POST)]
        [mmm.utils])
  (:require [clojure.string :as str]
            [ring.util.response :as ring]
            [ring.middleware [multipart-params :as mp]]
            [cemerick.friend :as friend]
            [cheshire.core :as cheshire]
            [monger.result :refer [acknowledged?]]
            [mmm.view.layout :as layout]
            [mmm.view.venues :as view]
            [mmm.model.venue :as model]))

(defn all [venues]
  (layout/common (view/all venues)))


(defn view [id]
  (let [venue (model/getByID id)]
    (layout/common (view/view venue) (str "Coming Soon to " (:name venue) "!"))))

(defn edit [id]
  (layout/common (view/edit (model/getByID id))))

(defn add [venue-map]
  (let [result (model/add venue-map)]
    (if (acknowledged? result)
      {:status 200
       :body "Write success!"}
      {:status 500
       :body "Error writing results to db"})))

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
  (POST "/venues/all" []  (cheshire/generate-string (model/all)))
  (POST "/venues/add" [& params]  (all params))
  (POST ["/venues/update/:id" :id #"[0-9a-f]+"] [id & params] (update id params))
  (GET ["/venues/delete/:id" :id #"[0-9a-f]+"] [id] (friend/authorize #{"admin"}) (delete id)))
