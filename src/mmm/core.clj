(ns mmm.core
  (:use [compojure.core :only (defroutes GET)]
        [ring.adapter.jetty :as ring])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [mmm.controller.movies :as movies]
            [mmm.view.layout :as layout]))

(defroutes routes
  movies/routes
  (route/resources "/")
  (route/not-found (layout/four-oh-four)))

(defn start [port]
  (run-jetty #'routes {:port port :join? false}))


(defn -main []
  (let [port (Integer/parseInt
                (or (System/getenv "PORT") "8080"))]
    (start port)))