(ns mmm.core
  (:use [compojure.core :only (defroutes GET)]
        [ring.middleware params
                         keyword-params
                         session
                         stacktrace
                         json]
        [ring.util.response :as response]
        [ring.adapter.jetty :as ring]
        [mmm.utils])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [cemerick.friend :as friend]
            (cemerick.friend [workflows :as workflows]
                             [credentials :as creds])
            [monger.core :as mg]
            [monger.ring.session-store :refer [session-store]]
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
            [mmm.auth :as auth]
            [mmm.cal :as cal]
            [mmm.digest :as digest]
            [mmm.scheduled :as sched]
            [mmm.special :as special]))

(defn wrappers [routes]
  (let [store (session-store db/db "sessions")]
    (-> routes
        (friend/authenticate {:login-uri     "/login"
                              :credential-fn #(creds/bcrypt-credential-fn auth/get-user-by-username %)
                              :workflows     [(workflows/interactive-form)]})
        wrap-keyword-params
        wrap-params
        wrap-json-response
        ;wrap-stacktrace
        (wrap-session {:store store}))))


(defn index
  ([] (layout/common (index/index))))

(defn about
  ([] (layout/common (index/about))))

(defn login
  [] (layout/common (login/login)))

(defn admin
  ([] (layout/common (admin/admin))))

(defn digest
  ([month] (layout/common (digest/digest-by-month month))))

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
  (GET ["/digest/:year/:month" :year #"[0-9]+" :month #"[0-9]+"] [year month] (render-request digest/digest-by-month (read-string year) (read-string month)))
  (route/resources "/"))


(defn handler [request]
  ((wrappers #'routes) request))

(defn start [port]
  (run-jetty handler {:port port :join? false}))


(defn -main []
  (let [port (Integer/parseInt
              (or (System/getenv "PORT") "3449"))]
    (do
      (sched/init-schedules)
      (start port))))

