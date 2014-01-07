(ns mmm.controller.screening
  (:use [compojure.core :only (defroutes GET POST)]
        [mmm.utils])
  (:require [clojure.string :as str]
            [mmm.view.layout :as layout]
            [ring.util.response :as ring]
            [mmm.view.screenings :as view]
            [mmm.model.screening :as model]))



(defn view [id]
  (layout/common (view/view (model/getByID (Long/parseLong id))))
  )

(defn all [screenings]
  (layout/common (view/all screenings))
  )

(defn addForm []
  (layout/common (view/add)))


(defn add [params]
  (do
    (model/add params)
    (ring/redirect "/")))


(defroutes routes
  (GET ["/screenings/:id" :id #"\d+"] [id] (render-request view id))
  (GET "/screenings/" [] (render-request all (model/all)))
  (GET "/screenings/add" [] (render-request addForm))
  (POST "/screenings/add" [& params] (add params))
  )

