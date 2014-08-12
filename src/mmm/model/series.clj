(ns mmm.model.series
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [mmm.model.db :as local]))

(defn add [name website description]
  (mc/insert local/db "series"
                {:name name :website website :description description}))

(defn all []
  (mc/find-maps local/db "series"))

(defn getByID [id]
  (local/getItemByID "series" id))
