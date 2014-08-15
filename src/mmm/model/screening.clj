(ns mmm.model.screening
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.joda-time]
            [monger.operators :refer :all]
            [mmm.utils :as utils]
            [clj-time.core :as time]
            [clj-time.coerce :as coerce]
            [clj-time.format :as time-fm]
            [mmm.model.db :as local]
            [mmm.model.venue :as venue]
            [mmm.model.movie :as movie]
            [mmm.model.series :as series]
            [mmm.model.presenter :as presenter]))


(defn vectorize [items]
  (if (vector? items)
    items
    [items]))


(defn add [screening-map]
  (prn screening-map)
  (let [showtimes-vec (vectorize (:showtime screening-map))
        showtimes (utils/earliest-first (map utils/read-showtime showtimes-vec))
        screening (assoc screening-map :showtime showtimes)]
  (:_id (mc/insert-and-return local/db "screenings" screening))))

(defn detailed-screening [screening]
  (let [venue_id (:venue_id screening)
        series_id (:series_id screening)
        presenter_ids (:presenter_id screening)
        movie_ids (:movie_id screening)]
    {
     :_id (str (:_id screening))
     :price (:price screening)
     :notes (:notes screening)
     :showtime (:showtime screening)
     :venue (venue/getByID venue_id)
     :series (series/getByID series_id)
     :movies (map movie/getByID (vectorize movie_ids))
     :presenters (map presenter/getByID (vectorize presenter_ids))
     }
    ))



(defn all []
  (map detailed-screening (local/all "screenings")))

(defn screenings-in-range [in out]
  (map
   detailed-screening
   (mc/find-maps
    local/db
    "screenings"
    {:showtime {$elemMatch {$gte in $lt out}}})))

(defn screenings-after-date [in]
    (map
   detailed-screening
   (mc/find-maps
    local/db
    "screenings"
    {:showtime {$elemMatch {$gte in}}})))

(defn current []
  (screenings-after-date (utils/right-now)))

(defn thisWeek []
 (screenings-in-range (utils/right-now) (utils/end-of-this-week)))

(defn nextWeek []
 (screenings-in-range (utils/beginning-of-next-week) (utils/end-of-next-week)))

(defn comingSoon []
 (screenings-in-range (utils/beginning-of-the-week-after-next) (utils/two-to-four-weeks-out)))

(defn getByID [id]
  (detailed-screening (local/getItemByID "screenings" id)))

(defn getByVenue [venue_id]
  (map detailed-screening (local/getRelations "screenings" :venue_id venue_id)))

;; (defn delete [id]
;;   (korma/delete local/screening
;;                 (korma/where (:id [= id]))))
