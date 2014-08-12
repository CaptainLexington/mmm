(ns mmm.model.db
  (:require
   [monger.core :as mg]
   [monger.collection :as mc])
  (:import org.bson.types.ObjectId))

(def conn (mg/connect))

(def db (mg/get-db conn "mmm"))

(defn getItemByID [item id]
  (mc/find-one-as-map db item { :_id (ObjectId. id)}))
