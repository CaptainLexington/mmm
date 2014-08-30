(ns mmm.cal
  (:require [clj-ical.format :as ical]
            [clj-time.core :as time]
            [clj-time.format :as tf]
            [mmm.utils :as utils]
            [mmm.model.screening :as screenings]))


(defn total-running-time [screening]
  (let [runningTimes (map #(read-string (:runningTime %)) (:movies screening))]
    (reduce + runningTimes)))

(defn event-from-showtime [showtime runtime title location]
  (let [end-time (time/plus showtime (time/minutes runtime))]
    (str "BEGIN:VEVENT\n"
         "DTSTART:" (tf/unparse (tf/formatters :basic-date-time-no-ms) showtime) "\n"
         "DTEND:" (tf/unparse (tf/formatters :basic-date-time-no-ms) end-time) "\n"
         "SUMMARY:" title "\n"
         "LOCATION:" location "\n"
         "END:VEVENT" "\n")))

(defn events-from-screening [screening]
  (apply str (map #(event-from-showtime
                    %
                    (total-running-time screening)
                    (if (nil? (:title screening))
                      (:title screening)
                      (utils/stringify-items (map :title (:movies screening))))
                    (:address (:venue screening)))
                  (:showtime screening))))

(defn cal []
  (let [screenings (screenings/current)]
    (str
     "BEGIN:VCALENDAR\n"
     (apply str (map events-from-screening screenings))
     "END:VCALENDAR\n")))

(cal)

