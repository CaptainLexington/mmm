(ns mmm.controller.movie
  (:use [compojure.core :only (defroutes GET POST)]
        [mmm.utils])
  (:require [clojure.string :as str]
            [cheshire.core :as cheshire]
            [ring.util.response :as ring]
            [ring.middleware [multipart-params :as mp]]
            [monger.result :refer [acknowledged?]]
            [cemerick.friend :as friend]
            [medley.core :as medley]
            [clj-fuzzy.metrics :as fuzzy]
            [mmm.view.layout :as layout]
            [mmm.view.movies :as view]
            [mmm.model.movie :as model]
            [mmm.moviedb :as mdb]))



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

(defn search [string]
  (let [results (medley/distinct-by 
                  #(str (:title %) " " (:year %))
                  (concat (mdb/search string)
                        (model/search string)))]
    (reverse (sort-by #(fuzzy/dice (:title %) string) results))))


(defroutes routes
  (GET "/movies/all" [] (render-request all (model/all)))
  (GET ["/movies/edit/:id" :id #"[0-9a-f]+"] [id] (friend/authorize #{"admin"}) (render-request edit id))
  (GET "/movies/search" [& params] (search (:term params)))
  (POST "/movies/add" [& params] (add params))
  (POST "/movies/all" [] (cheshire/generate-string (model/all)))
  (POST ["/movies/update/:id" :id #"[0-9a-f]+"] [id & params] (update id params)))
