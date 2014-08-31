(ns mmm.cal
  (:require [clj-ical.format :as ical]
            [clj-time.core :as time]
            [clj-time.format :as tf]
            [clj-time.local :as tl]
            [mmm.utils :as utils]
            [mmm.model.screening :as screenings]))


(defn total-running-time [screening]
  (let [runningTimes (map #(read-string (:runningTime %)) (:movies screening))]
    (reduce + runningTimes)))

(defn event-from-showtime [showtime runtime title location id]
  (let [end-time (time/plus showtime (time/minutes runtime))]
    (str "BEGIN:VEVENT" \return \newline
         ;"DTSTART:" (tf/unparse (tf/formatters :basic-date-time-no-ms) showtime) \return \newline
         ;"DTEND:" (tf/unparse (tf/formatters :basic-date-time-no-ms) end-time) \return \newline
         "DTSTART:" (utils/display-date-and-time showtime) \return \newline
         "DTEND:" (utils/display-date-and-time end-time) \return \newline
         "SUMMARY:\"" title "\"" \return \newline
         "LOCATION:\"" location "\"" \return \newline
         "DESCRIPTION:http://www.midnightmoviesmpls.com/screenings/" id \return \newline
         "END:VEVENT" \return \newline)))


(defn events-from-screening [screening]
  (apply str (map #(event-from-showtime
                    %
                    (total-running-time screening)
                    (if (nil? (:title screening))
                      (:title screening)
                      (apply str (utils/listify-items (map :title (:movies screening)))))
                    (:address (:venue screening))
                    (:_id screening))
                  (:showtime screening))))

(defn cal []
  (clojure.string/replace
   (let [screenings (screenings/current)]
     (str
      "BEGIN:VCALENDAR" \return \newline
      "VERSION:2.0" \return \newline
      "BEGIN:VTIMEZONE" \return \newline
      "TZID:CST" \return \newline
      "BEGIN:STANDARD" \return \newline
      "TZNAME:CST" \return \newline
      "TZOFFSETFROM:-0600" \return \newline
      "TZOFFSETTO:-0600" \return \newline
      "END:STANDARD" \return \newline
      "END:VTIMEZONE" \return \newline
      "PRODID:MindightMoviesMPLS" \return \newline
      (apply str (map events-from-screening screenings))
      "END:VCALENDAR" \return \newline))
   ","
   "\\,"))
