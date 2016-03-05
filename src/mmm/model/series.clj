(ns mmm.model.series
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [mmm.model.db :as local]))

(defn add [series-map]
  (mc/insert local/db "series"
             series-map))

(defn all []
  (reverse (local/all "series")))

(defn update [id series-map]
  (local/updateItemByID
   "series"
   series-map
   id))

(defn title [series]
  (str (:name series)))

(defn getByID [id]
  (local/getItemByID "series" id))

(defn delete [id]
  local/deleteByID "series" id)
