(ns mmm.model.db
  (:require
   [monger.core :as mg]
   [monger.collection :as mc]
   [mmm.config :as config])
  (:import org.bson.types.ObjectId))

(def uri (mg/connect-via-uri (str "mongodb://"
                                  config/mongo-user
                                  ":"
                                  config/mongo-password
                                  "@"
                                  config/mongo-url
                                  "/"
                                  config/mongo-db)))

(def conn (:conn uri))

(def db (:db uri))

(defn stringify-id [datamap]
  (assoc datamap :_id (str (:_id datamap))))

(defn all [coll]
  (map stringify-id (mc/find-maps db coll)))

(defn getItemByID [collection id]
  (when-not (= id "")
    (stringify-id (mc/find-map-by-id db collection (ObjectId. id)))))

(defn updateItemByID [collection item id]
  (mc/update-by-id db collection (ObjectId. id) item))

(defn getRelations [item relation id]
  (mc/find-maps db item {relation id}))


(defn deleteByID [collection id]
  (mc/remove-by-id db collection (ObjectId. id)))

