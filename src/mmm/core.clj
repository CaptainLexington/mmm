(ns mmm.core
  (:use [compojure.core :only (defroutes GET)]
        [ring.adapter.jetty :as ring]
        [mmm.utils])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [mmm.controller.movies :as movies]
            [mmm.view.layout :as layout]
            [mmm.view.index :as index]
            [mmm.model.movie :as movie]
            [mmm.model.screening :as screening]
            [mmm.model.db :as db]))


(defn index
  ([] (layout/common (index/index (screening/all)))))


(defroutes routes
  (GET "/" [] (render-request index))
  (route/resources "/"))

(defn start [port]
  (run-jetty #'routes {:port port :join? false}))

(defn init-db
  "runs when the application starts and updates the database scheme if necessary"
  []
  (if-not (db/actualized?)
    (db/actualize)))

(defn -main []
  (let [port (Integer/parseInt
                (or (System/getenv "PORT") "8080"))]
    (do
      (init-db)
      (start port))))