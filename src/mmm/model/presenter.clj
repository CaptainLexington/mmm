(ns mmm.model.presenter
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [mmm.model.db :as local]))

(defn add [name website description]
  (mc/insert local/db "presenters"
                {:name name :website website :description description}))

(defn getByID [id]
  (local/getItemByID "presenters" id))

(defn update [id presenter-map]
  (local/updateItemByID "presenters" presenter-map id))

(defn all []
  (sort-by :name (local/all "presenters")))

(defn delete [id]
  (local/deleteByID "presenters" id))
