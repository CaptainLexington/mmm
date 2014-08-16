(ns mmm.core
  (:use [compojure.core :only (defroutes GET)]
        [ring.middleware params
                         keyword-params]
        [ring.adapter.jetty :as ring]
        [mmm.utils])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [shoreleave.middleware.rpc :as rpc]
            [mmm.controller.movie :as movies]
            [mmm.controller.screening :as screenings]
            [mmm.controller.venue :as venues]
            [mmm.controller.series :as series]
            [mmm.controller.presenter :as presenter]
            [mmm.view.layout :as layout]
            [mmm.view.index :as index]
            [mmm.view.admin :as admin]
            [mmm.model.movie :as movie]
            [mmm.model.screening :as screening]
            [mmm.model.db :as db]
            [mmm.remotes :as remotes]))

(defn wrappers [routes]
  (-> routes
      rpc/wrap-rpc
      wrap-keyword-params
      wrap-params
      ))


(defn index
  ([] (layout/common (index/index (screening/all)))))

(defn about
  ([] (layout/common (index/about))))

(defn admin
  ([] (layout/common (admin/admin))))

;;ROUTING BRO

(defroutes routes
  venues/routes
  movies/routes
  screenings/routes
  series/routes
  presenter/routes
  (GET "/" [] (render-request index))
  (GET "/admin" [] (render-request admin))
  (GET "/about" [] (render-request about))
  (route/resources "/"))

(defn start [port]
  (run-jetty (wrappers #'routes) {:port port :join? false}))

;; (defn init-db
;;   "runs when the application starts and updates the database scheme if necessary"
;;   []
;;   (if-not (db/actualized?)
;;     (db/actualize)))

(defn -main []
  (let [port (Integer/parseInt
              (or (System/getenv "PORT") "8080"))]
    (do
      ;(init-db)
      (start port))))
