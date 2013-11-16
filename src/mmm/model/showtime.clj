(ns mmm.model.showtime
  (:require [korma.db :as db]
            [korma.core :as korma]
            [mmm.model.db :as local]))

(defn all []
  (korma/select local/showtime))

(defn add [date time]
  (korma/insert local/showtime
                (korma/values {:date date :time time})))


(defn delete [id]
  (korma/delete local/showtime
                (korma/where (:id [= id]))))