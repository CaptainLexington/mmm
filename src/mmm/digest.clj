(ns mmm.digest
	(:require [mmm.model.screening :as screenings]
			  [mmm.utils :as utils]
		      [clj-time.core :as time]))



(defn get-screenings-by-month [year month]
	(let [month-date (time/date-time year month)
		  first (time/first-day-of-the-month  month-date)
		  last (time/plus (time/last-day-of-the-month month-date) (time/days 2))]
		  (screenings/screenings-in-range first last)))


(defn get-dates [screening]
	(distinct (map #(time/day (utils/local-time %)) (:showtime screening))))


(defn get-venue [screening]
	(:name (:venue screening)))


(defn get-presenters [screening]
	(map :name (:presenters  screening)))

(defn extract-relevant-data [screening]
	{:title (screenings/screening-title screening)
	 :venue (get-venue screening)
	 :dates (get-dates screening)
	 :presenters (get-presenters screening)})


(defn create-listing-from-extract [extract]
	(str
		(:venue extract) 
		": "
		(:title extract)
		" - "
		(apply str (utils/listify-items (:dates extract)))
		(if (zero? (count (:presenters extract)))
			""
			(str " ("
				(clojure.string/join "/" (:presenters extract))
				")"))
		"<br/>"))


(defn digest-from-screenings [screenings]
	(let [sorted-screenings (sort-by get-venue screenings)
		  extracts (map extract-relevant-data  sorted-screenings)]
		(apply 
			str 
			(map 
				create-listing-from-extract 
				extracts))))


(defn digest-by-month [year month]
	(digest-from-screenings (get-screenings-by-month year month)))

