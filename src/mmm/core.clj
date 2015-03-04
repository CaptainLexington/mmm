(ns mmm.core
  (:use [compojure.core :only (defroutes GET)]
        [ring.middleware params
                         keyword-params
                         session]
        [ring.util.response :as response]
        [ring.adapter.jetty :as ring]
        [mmm.utils])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [cemerick.friend :as friend]
            (cemerick.friend [workflows :as workflows]
                             [credentials :as creds])
            [shoreleave.middleware.rpc :as rpc]
            [mmm.controller.movie :as movies]
            [mmm.controller.screening :as screenings]
            [mmm.controller.venue :as venues]
            [mmm.controller.series :as series]
            [mmm.controller.presenter :as presenter]
            [mmm.view.layout :as layout]
            [mmm.view.index :as index]
            [mmm.view.admin :as admin]
            [mmm.view.login :as login]
            [mmm.model.movie :as movie]
            [mmm.model.screening :as screening]
            [mmm.model.db :as db]
            [mmm.remotes :as remotes]
            [mmm.auth :as auth]
            [mmm.cal :as cal]
            [mmm.special :as special]))

(defn wrappers [routes]
  (-> routes
      (friend/authenticate {:login-uri "/login"
                            :credential-fn #(creds/bcrypt-credential-fn auth/get-user-by-username %)
                            :workflows [(workflows/interactive-form)]})
      rpc/wrap-rpc
      wrap-keyword-params
      wrap-params
      wrap-session))


(defn index
  ([] (layout/common (index/index (screening/all)))))

(defn about
  ([] (layout/common (index/about))))

(defn login
  [] (layout/common (login/login)))

(defn admin
  ([] (layout/common (admin/admin))))

(defn four-oh-four
  [] (layout/common "404 Not Found"))

;;ROUTING BRO

(defroutes routes
  venues/routes
  movies/routes
  screenings/routes
  series/routes
  presenter/routes
  special/routes
  (GET "/" [] (render-request index))
  (GET "/login" [] (render-request login))
  (GET "/admin" [] (friend/authorize #{"admin"}) (render-request admin))
  (GET "/about" [] (render-request about))
  (GET "/cal" [] (render-calendar cal/cal (screening/current)))
  (route/resources "/"))

(defn start [port]
  (run-jetty (wrappers #'routes) {:port port :join? false}))


(defn -main []
  (let [port (Integer/parseInt
              (or (System/getenv "PORT") "8080"))]
    (do
      (start port))))

