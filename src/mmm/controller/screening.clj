(ns mmm.controller.screening
  (:use [compojure.core :only (defroutes GET POST)]
        [mmm.utils])
  (:require [clojure.string :as str]
            [mmm.view.layout :as layout]
            [ring.util.response :as ring]
            [mmm.view.screenings :as view]
            [mmm.model.screening :as model]))



(defn view [id]
  (layout/common (view/view (model/getByID id))))

(defn all [screenings]
  (layout/common (view/all screenings)))

(defn addForm []
  (layout/common (view/add)))

(defn edit [id]
  (layout/common (view/edit (model/getByID id))))

(defn add [params]
  (do
    (model/add params)
    (ring/redirect "/")))


(defroutes routes
  (GET "/screenings/add" [] (render-request addForm))
  (GET ["/screenings/:id" :id #"[0-9a-f]+"] [id] (render-request view id))
  (GET "/screenings/" [] (render-request all (model/all)))
  (GET ["/screenings/edit/:id" :id #"[0-9a-f]+"] [id] (render-request edit id))
  (POST "/screenings/add" [& params] (add params)))

