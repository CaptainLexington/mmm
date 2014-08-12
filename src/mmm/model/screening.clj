(ns mmm.model.screening
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [mmm.utils :as utils]
            [clj-time.core :as time]
            [clj-time.coerce :as coerce]
            [clj-time.format :as time-fm]
            [mmm.model.db :as local]
            [mmm.model.venue :as venue]
            [mmm.model.movie :as movie]
            [mmm.model.series :as series]
            [mmm.model.presenter :as presenter]))




(defn add [screening-map]
  (mc/insert local/db "screenings" screening-map))

(defn detailed-screening [screening]
  (let [venue_id (:venue_id screening)
        series_id (:series_id screening)
        presenter_ids (:presenter_id screening)
        movie_ids (:movie_id screening)]
    {
     :price (:price screening)
     :notes (:notes screening)
     :datetimes (:datetimes screening)
     :venue (venue/getByID venue_id)
     :series (series/getByID series_id)
     :movies (movie/getByID movie_ids)
     :presenters (map presenter/getByID presenter_ids)
     }
    ))


(defn all []
  (map detailed-screening (mc/find-maps local/db "screenings")))


(defn thisWeek []
  ;;   (distinct (korma/select local/screening
  ;;                           (korma/with local/movie)
  ;;                           (korma/with local/venue)
  ;;                           (korma/with local/showtime
  ;;                                       (korma/order :date)
  ;;                                       (korma/order :time))
  ;;                           (korma/join local/showtime
  ;;                                       (= :showtime.screening_id :id))
  ;;                           (korma/order :showtime.date :asc)
  ;;                           (korma/where (and
  ;;                                         (>= :showtime.date (coerce/to-sql-date (time/today)))
  ;;                                         (<= :showtime.date (coerce/to-sql-date (utils/end-of-this-week)))))
  ;;                           )))
  )

(defn nextWeek []
  ;;   (distinct (korma/select local/screening
  ;;                           (korma/with local/movie)
  ;;                           (korma/with local/venue)
  ;;                           (korma/with local/showtime
  ;;                                       (korma/order :date)
  ;;                                       (korma/order :time))
  ;;                           (korma/join local/showtime
  ;;                                       (= :showtime.screening_id :id))
  ;;                           (korma/order :showtime.date :asc)
  ;;                           (korma/where (and
  ;;                                         (> :showtime.date (coerce/to-sql-date (utils/end-of-this-week)))
  ;;                                         (<= :showtime.date (coerce/to-sql-date (utils/end-of-next-week)))))
  ;;                           )))
  )

(defn comingSoon []
  ;;   (distinct (korma/select local/screening
  ;;                           (korma/with local/movie)
  ;;                           (korma/with local/venue)
  ;;                           (korma/with local/showtime
  ;;                                       (korma/order :date)
  ;;                                       (korma/order :time))
  ;;                           (korma/join local/showtime
  ;;                                       (= :showtime.screening_id :id))
  ;;                           (korma/order :showtime.date :asc)
  ;;                           (korma/where (and
  ;;                                         (> :showtime.date (coerce/to-sql-date (utils/end-of-next-week)))
  ;;                                         (<= :showtime.date (coerce/to-sql-date (utils/two-to-four-weeks-out)))))
  ;;                           )))
  )

(defn getByID [id]
  (detailed-screening (local/getItemByID "screenings" id)))

(defn getByVenue [venue_id]
  ;;   (do
  ;;     (prn venue_id)
  ;;     (korma/select local/screening
  ;;                   (korma/with local/movie)
  ;;                   (korma/with local/venue)
  ;;                   (korma/with local/showtime
  ;;                               (korma/order :date)
  ;;                               (korma/order :time))
  ;;                   (korma/where (= :venue_id venue_id))))
  )


;; (defn delete [id]
;;   (korma/delete local/screening
;;                 (korma/where (:id [= id]))))
