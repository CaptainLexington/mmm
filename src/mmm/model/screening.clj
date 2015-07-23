(ns mmm.model.screening
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.joda-time]
            [monger.operators :refer :all]
            [clojure.string :as string]
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
  (if (or (vector? items) (nil? items))
    items
    [items]))

(defn earliest-future-date [showtimes]
  (first (filter #(time/after? % (utils/right-now)) showtimes)))

(defn sort-by-date [screenings]
  (sort-by #(earliest-future-date (:showtime %)) screenings))

(defn process-screening-map [screening-map]
  (let [showtimes-vec (vectorize (:showtime screening-map))
        showtimes (utils/earliest-first (map utils/read-showtime showtimes-vec))
        screening (assoc screening-map :showtime showtimes)]
    screening))



(defn add [screening-map]
  (:_id (mc/insert-and-return local/db "screenings" (process-screening-map screening-map))))

(defn update [id screening-map]
  (local/updateItemByID "screenings" (process-screening-map screening-map) id))

(defn delete [id]
  (local/deleteByID "screenings" id))

(defn detailed-screening [screening]
  (let [venue_id (:venue_id screening)
        series_id (:series_id screening)
        presenter_ids (:presenter_id screening)
        movie_ids (:movie_id screening)]
    (assoc screening
      :_id (str (:_id screening))
      :venue (venue/getByID venue_id)
      :series (series/getByID series_id)
      :movies (map movie/getByID (vectorize movie_ids))
      :presenters (map presenter/getByID (vectorize presenter_ids)))))

(defn all []
  (reverse (sort-by-date (map detailed-screening (local/all "screenings")))))

(defn screenings-in-range [in out]
  (sort-by-date
    (map
      detailed-screening
      (mc/find-maps
        local/db
        "screenings"
        {:showtime {$elemMatch {$gte in $lt out}}}))))

(defn screenings-after-date [in]
  (sort-by-date
    (map
      detailed-screening
      (mc/find-maps
        local/db
        "screenings"
        {:showtime {$elemMatch {$gte in}}}))))

(defn screenings-after-date-by-relation [in relation id]
  (sort-by-date
    (map
      detailed-screening
      (mc/find-maps
        local/db
        "screenings"
        {:showtime {$elemMatch {$gte in}}
         relation (if (= relation :presenter_id)
                    id
                    {$in [id]})}))))

(defn current []
  (screenings-after-date (utils/right-now)))

(defn current-by-venue [venue_id]
  (screenings-after-date-by-relation (utils/right-now) :venue_id venue_id))

(defn current-by-presenter [presenter_id]
  (screenings-after-date-by-relation (utils/right-now) :presenter_id presenter_id))

(defn current-by-series [series_id]
  (screenings-after-date-by-relation (utils/right-now) :series_id series_id))

(current-by-series "53edc27e6d6e2828675998bd")


(defn one-day [day]
  (screenings-in-range (utils/start-of-day day) (utils/end-of-day day)))

(defn today []
  (one-day (utils/right-now)))

(defn tomorrow []
  (one-day (time/plus (utils/right-now)
                      (time/days 1))))

(defn thisWeek []
  (screenings-in-range (utils/right-now) (utils/end-of-this-week)))

(defn nextWeek []
  (screenings-in-range (utils/beginning-of-next-week) (utils/end-of-next-week)))


(defn comingSoon []
  (screenings-in-range (utils/beginning-of-the-week-after-next) (utils/two-to-four-weeks-out)))

(defn getByID [id]
  (detailed-screening (local/getItemByID "screenings" id)))

(defn getByVenue [venue_id]
  (sort-by-date (map detailed-screening (local/getRelations "screenings" :venue_id venue_id))))


(defn screening-title [screening]
  (let [movie-titles (map :title (:movies screening))]
    (if (= (:title screening) "")
           (apply str (utils/listify-items movie-titles))
           (:title screening))))

(defn title-by-screening [screening]
  (let [venue-name (:name (:venue screening))]
    (str (screening-title screening) 
         " at "
         venue-name)))


(defn title [id]
  (title-by-screening (getByID id)))

(defn twitter-handle-or-name [venue]
  (if (:twitter-handle venue)
    (str "@" (:twitter-handle venue))
    (:name venue)))

(defn generate-tweet-text
  ([screening]
   (generate-tweet-text (map :_id (:movies screening))
                        (map :_id (:presenters screening))
                        (:_id (:venue screening))))
  ([movie-ids presenter-ids venue-id]
     (let [movie-titles (map #(-> %
                                  movie/getByID
                                  :title)
                             movie-ids)
           presenter-names (map #(-> %
                                     presenter/getByID
                                     twitter-handle-or-name)
                                presenter-ids)
           venue-name (-> venue-id
                          venue/getByID
                          twitter-handle-or-name)]
       (str
         (string/join
           " "
           presenter-names)
         (case (count presenter-names)
           0 ""
           1 " presents "
           " present ")
         (string/join
           " "
           movie-titles)
         " at "
         venue-name
         ))))


;; (defn delete [id]
;;   (korma/delete local/screening
;;                 (korma/where (:id [= id]))))



