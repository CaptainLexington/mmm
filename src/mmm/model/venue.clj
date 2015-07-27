(ns mmm.model.venue
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [mmm.model.db :as local]))

(defn all []
  (sort-by :name (local/all "venues")))

(defn add [venue-map]
  (mc/insert
   local/db
   "venues"
   venue-map))

(defn update [id venue-map]
  (local/updateItemByID
   "venues"
   venue-map
   id))

(defn short-name [venue]
  (if (:short-name venue)
    (:short-name venue)
    (:name venue)))

(defn getByID [id]
  (local/getItemByID "venues" id))

 (defn delete [id]
   (local/deleteByID "venues" id))
