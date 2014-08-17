(ns mmm.controller.screening
  (:use [compojure.core :only (defroutes GET POST)]
        [mmm.utils])
  (:require [clojure.string :as str]
            [cemerick.friend :as friend]
            [mmm.view.layout :as layout]
            [ring.util.response :as ring]
            [mmm.view.screenings :as view]
            [mmm.model.screening :as model]))



(defn view [id]
  (layout/common (view/view (model/getByID id))))

(defn all [screenings]
  (layout/common (view/all screenings)))

(defn current [screenings]
  (layout/common (view/all screenings :edit)))

(defn addForm []
  (layout/common (view/add)))

(defn edit [id]
  (layout/common (view/edit (model/getByID id))))

(defn update [id params]
  (model/update id params)
  (ring/redirect (str "/screenings/" id)))

(defn add [params]
  (ring/redirect
   (str "/screenings/"
   (model/add params))))

(defn delete [id]
  (model/delete id)
  (ring/redirect "/screenings/all"))

(defroutes routes
  (GET "/screenings/add" [] (friend/authorize #{"admin"}) (render-request addForm))
  (GET ["/screenings/:id" :id #"[0-9a-f]+"] [id] (render-request view id))
  (GET "/screenings/all" [] (friend/authorize #{"admin"}) (render-request all (model/all)))
  (GET "/screenings/" [] (render-request current (model/current)))
  (GET ["/screenings/edit/:id" :id #"[0-9a-f]+"] [id] (friend/authorize #{"admin"}) (render-request edit id))
  (POST ["/screenings/update/:id" :id #"[0-9a-f]+"] [id & params] (update id params))
  (POST "/screenings/add" [& params] (add params))
  (GET ["/screenings/delete/:id" :id #"[0-9a-f]+"] [id] (friend/authorize #{"admin"}) (delete id)))

