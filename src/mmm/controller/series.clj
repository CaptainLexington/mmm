(ns mmm.controller.series
  (:use [compojure.core :only (defroutes GET POST)]
        [mmm.utils])
  (:require [clojure.string :as str]
            [ring.util.response :as ring]
            [ring.middleware [multipart-params :as mp]]
            [mmm.files :as files]
            [mmm.view.layout :as layout]
            [mmm.view.series :as view2]
            [mmm.model.series :as model2]))

(defn all [series]
  (layout/common (view2/all series)))


;; (defn view [id]
;;   (layout/common (view/view (model/getByID id))))

;; (defn edit [id]
;;   (layout/common (view/edit (model/getByID id))))

;; (defn update [id venue-map]
;;   (model/update id venue-map)
;;   (ring/redirect (str "/venues/" id)))


(defroutes routes
  ;(GET ["/venues/:id" :id #"[0-9a-f]+"] [id] (render-request view id)))
 (GET "/series/all" [] (render-request all (model2/all))))
;  (GET ["/venues/edit/:id" :id #"[0-9a-f]+"] [id] (render-request edit id))
;  (POST ["/venues/update/:id" :id #"[0-9a-f]+"] [id & params] (update id params)))
