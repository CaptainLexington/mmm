(ns mmm.model.screening
  (:require [korma.db :as db]
            [mmm.utils :as utils]
            [clj-time.core :as time]
            [clj-time.coerce :as coerce]
            [korma.core :as korma]
            [mmm.model.db :as local]))

(defn all []
  (distinct (korma/select local/screening
                          (korma/with local/movie)
                          (korma/with local/venue)
                          (korma/with local/showtime
                                      (korma/order :date))
                          (korma/join local/showtime
                                      (= :showtime.screening_id :id))
                          (korma/order :showtime.date :ASC)
                          (korma/where (>= :showtime.date (coerce/to-sql-date (time/today)))))))

(defn thisWeek []
   (distinct (korma/select local/screening
                           (korma/with local/movie)
                           (korma/with local/venue)
                          (korma/with local/showtime
                                      (korma/order :date))
                           (korma/join local/showtime
                                       (= :showtime.screening_id :id))
                           (korma/order :showtime.date :asc)
                           (korma/where (and
                                         (>= :showtime.date (coerce/to-sql-date (time/today)))
                                         (<= :showtime.date (coerce/to-sql-date (utils/end-of-this-week)))))
                           )))

(defn nextWeek []
  (distinct (korma/select local/screening
                          (korma/with local/movie)
                          (korma/with local/venue)
                          (korma/with local/showtime
                                      (korma/order :date))
                          (korma/join local/showtime
                                      (= :showtime.screening_id :id))
                          (korma/order :showtime.date :asc)
                          (korma/where (and
                                        (> :showtime.date (coerce/to-sql-date (utils/end-of-this-week)))
                                        (<= :showtime.date (coerce/to-sql-date (utils/end-of-next-week)))))
                          )))

(defn comingSoon []
  (distinct (korma/select local/screening
                          (korma/with local/movie)
                          (korma/with local/venue)
                          (korma/with local/showtime
                                      (korma/order :date))
                          (korma/join local/showtime
                                      (= :showtime.screening_id :id))
                          (korma/order :showtime.date :asc)
                          (korma/where (and
                                        (> :showtime.date (coerce/to-sql-date (utils/end-of-next-week)))
                                        (<= :showtime.date (coerce/to-sql-date (utils/two-to-four-weeks-out)))))
                          )))







(defn getByID [id]
  (korma/select local/screening
                (korma/with local/movie)
                (korma/with local/venue)
                (korma/with local/showtime
                            (korma/order :date))
                (korma/where (= :id id)))
  )

(defn add [screening-map]
  (let [screening_id
        (:id (korma/insert local/screening
                           (korma/values {:venue_id (read-string (:venue_id screening-map))})))]
    (do
      (prn screening-map)
      (doseq [movie_id (:movie_id screening-map)]
        (korma/insert local/movies-screenings
                      (korma/values {:screening_id screening_id :movie_id (read-string (str movie_id))})))
      )))

(defn delete [id]
  (korma/delete local/screening
                (korma/where (:id [= id]))))