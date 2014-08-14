(ns mmm.model.db
  (:require
   [monger.core :as mg]
   [monger.collection :as mc])
  (:import org.bson.types.ObjectId))

(def conn (mg/connect))

(def db (mg/get-db conn "mmm"))

(defn stringify-id [datamap]
  (assoc datamap :_id (str (:_id datamap))))

(defn all [coll]
  (map stringify-id (mc/find-maps db coll)))

(defn getItemByID [item id]
  (when-not (= id "")
    (stringify-id (mc/find-map-by-id db item (ObjectId. id)))))

(defn getRelations [item relation id]
  (prn item)
  (prn id)
  (mc/find-maps db item {relation id}))
