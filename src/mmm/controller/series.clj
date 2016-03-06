(ns mmm.controller.series
  (:use [compojure.core :only (defroutes GET POST)]
        [mmm.utils])
  (:require [clojure.string :as str]
            [ring.util.response :as ring]
            [ring.middleware [multipart-params :as mp]]
            [cemerick.friend :as friend]
            [cheshire.core :as cheshire]
            [monger.result :refer [acknowledged?]]
            [mmm.view.layout :as layout]
            [mmm.view.series :as view]
            [mmm.model.series :as model]))

(defn all [series]
  (layout/common (view/all series)))


(defn view [id]
  (let [series (model/getByID id)]
    (layout/common (view/view series) (model/title series))))

(defn edit [id]
  (layout/common (view/edit (model/getByID id))))

(defn add [series-map]
  (let [result (model/add series-map)]
    (if (acknowledged? result)
      {:status 200
       :body "Write success!"}
      {:status 500
       :body "Error writing results to db"})))

(defn update [id venue-map]
  (model/update id venue-map)
  (ring/redirect (str "/series/" id)))

(defn delete [id]
  (model/delete id)
  (ring/redirect "/series/all"))


(defroutes routes
  (GET ["/series/:id" :id #"[0-9a-f]+"] [id] (render-request view id))
  (GET "/series/all" [] (friend/authorize #{"admin"}) (render-request all (model/all)))
  (POST "/series/all" []  (cheshire/generate-string (model/all)))
  (POST "/series/add" [& params]  (add params))
  (GET ["/series/edit/:id" :id #"[0-9a-f]+"] [id] (friend/authorize #{"admin"}) (render-request edit id))
  (POST ["/series/update/:id" :id #"[0-9a-f]+"] [id & params] (update id params))
  (GET ["/series/delete/:id" :id #"[0-9a-f]+"] [id] (friend/authorize #{"admin"}) (render-request delete id)))
