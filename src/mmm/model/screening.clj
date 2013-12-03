(ns mmm.model.screening
  (:require [korma.db :as db]
            [korma.core :as korma]
            [mmm.model.db :as local]))

(defn all []
  (korma/select local/screening
                (korma/with local/showtime)
                (korma/with local/movie)
                (korma/with local/venue)))

(defn getByID [id]
  (korma/select local/screening
                (korma/with local/movie)
                (korma/with local/venue)
                (korma/with local/showtime)
                (korma/where (= :id id)))
  )

(defn add [screening-map]
  (korma/insert local/screening
                (korma/values {:venue_id (Long/parseLong (:venue_id screening-map))})))


(defn delete [id]
  (korma/delete local/screening
                (korma/where (:id [= id]))))