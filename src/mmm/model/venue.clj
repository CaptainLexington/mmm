(ns mmm.model.venue
  (:require [korma.db :as db]
            [korma.core :as korma]
            [mmm.model.db :as local]))

(defn all []
  (korma/select local/venue))

(defn add [name address description website phone]
  (korma/insert local/venue
                (korma/values {:name name :address address :description description :website website :phoneNumber phone})))

(defn getByID [id]
  (korma/select local/venue
                (korma/where (= :id id))))

(defn delete [id]
  (korma/delete local/venue
                (korma/where (:id [= id]))))