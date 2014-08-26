(ns mmm.cal
  (:require [clj-ical.format :as ical]
            [clj-time.core :as time]
            [clj-time.format :as tf]
            [mmm.utils :as utils]
            [mmm.model.screening :as screenings]))


(defn total-running-time [screening]
  (reduce + (map #(read-string (:runningTime %)) (:movies screening))))

(defn event-from-showtime [showtime runtime title location]
  (let [end-time (time/plus showtime (time/minutes runtime))]
    [:vevent
     [:dtstart (tf/unparse (tf/formatters :basic-date-time-no-ms) showtime)]
     [:dtend (tf/unparse (tf/formatters :basic-date-time-no-ms) end-time)]
     [:summary title]
     [:location location]]))

(defn events-from-screening [screening]
  (into [] (map #(event-from-showtime
                  %
                  (total-running-time screening)
                  (if (nil? (:title screening))
                    (:title screening)
                    (utils/stringify-items (map :title (:movies screening))))
                  (:address (:venue screening)))
                (:showtime screening))))

(defn cal []
  (let [screenings (screenings/current)]
    (concat
    [:vcalendar]
     (into [] (apply concat (map events-from-screening screenings))))))
