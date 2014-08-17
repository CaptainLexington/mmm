(ns mmm.auth
  (:require [cemerick.friend :as friend]
            (cemerick.friend [workflows :as workflows]
                             [credentials :as creds])
            [monger.core :as mg]
            [monger.collection :as mc]
            [mmm.model.db :as db]))

(mc/insert db/db "users" {:username "dale" :password (creds/hash-bcrypt "test") :roles #{::admin}})


(defn all-users []
  (mc/find-maps db/db "users"))

(defn get-user-by-username [username]
  (mc/find-one-as-map db/db "users" {:username username}))

(defn authenticate-user [user]
  (prn user)
  (let [verified-user (get-user-by-username (:username user))]
    (if (= (:password verified-user) (:password user))
      verified-user
      nil)))
