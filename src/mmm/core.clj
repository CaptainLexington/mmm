(ns mmm.core
  (:use [compojure.core :only (defroutes GET)]
        [ring.adapter.jetty :as ring]
        [mmm.utils])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [mmm.controller.movies :as movies]
            [mmm.view.layout :as layout]
            [mmm.model.movie :as movie]))


(defn index
  ([] (layout/common (layout/index (movie/all)))))


(defroutes routes
  (GET "/" [] (render-request index))
  (route/resources "/"))

(defn start [port]
  (run-jetty #'routes {:port port :join? false}))


(defn -main []
  (let [port (Integer/parseInt
                (or (System/getenv "PORT") "8080"))]
    (start port)))