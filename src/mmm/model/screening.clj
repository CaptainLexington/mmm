(ns mmm.model.screening
  (:require [korma.db :as db]
            [korma.core :as korma]
            [mmm.model.db :as local]))

(defn all []
  (korma/select local/screening
                (korma/with local/showtime)
                (korma/with local/movie)
                (korma/with local/venue)))

(defn add [date time]
  (korma/insert local/screening
                (korma/values {:date date :time time})))


(defn delete [id]
  (korma/delete local/screening
                (korma/where (:id [= id]))))