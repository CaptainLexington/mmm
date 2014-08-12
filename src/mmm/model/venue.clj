(ns mmm.model.venue
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [mmm.model.db :as local]))

(defn all []
  (mc/find-maps local/db "venues"))

(defn add [name address description website phone]
  (mc/insert local/db "venues"
             {:name name :address address :description description :website website :phoneNumber phone}))

(defn getByID [id]
  (local/getItemByID "venues" id))

;; (defn delete [id]
;;   (korma/delete local/venue
;;                 (korma/where (:id [= id]))))
