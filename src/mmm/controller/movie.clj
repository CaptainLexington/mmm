(ns mmm.controller.movie
  (:use [compojure.core :only (defroutes GET POST)]
        [mmm.utils])
  (:require [clojure.string :as str]
            [cheshire.core :as cheshire]
            [ring.util.response :as ring]
            [ring.middleware [multipart-params :as mp]]
            [monger.result :refer [acknowledged?]]
            [cemerick.friend :as friend]
            [mmm.view.layout :as layout]
            [mmm.view.movies :as view]
            [mmm.model.movie :as model]))



(defn all [movies]
  (layout/common (view/all movies)))

(defn edit [id]
  (layout/common (view/edit (model/getByID id))))

(defn add [movie-map]
  (let [result (model/add movie-map)]
    (if (acknowledged? result)
      {:status 200
       :body "Write success!"}
      {:status 500
       :body "Error writing results to db"})))

(defn update [id movie-map]
  (model/update id movie-map)
  (ring/redirect "/admin"))

(defroutes routes
  (GET "/movies/all" [] (render-request all (model/all)))
  (GET ["/movies/edit/:id" :id #"[0-9a-f]+"] [id] (friend/authorize #{"admin"}) (render-request edit id))
  (POST "/movies/add" [& params] (add params))
  (POST "/movies/all" [] (cheshire/generate-string (model/all)))
  (POST ["/movies/update/:id" :id #"[0-9a-f]+"] [id & params] (update id params)))
