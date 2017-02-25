(ns mmm.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]
            [clojure.string :as string]
            [clojure.set :as set]
            [camel-snake-kebab.core :as csk]
            [cljs-time.core :refer [at-midnight plus hours minutes]]
            [cljs-time.format :refer [unparse formatter-local]]))

(re-frame/reg-sub
  :name
  (fn [db]
    (:name db)))

(re-frame/reg-sub
  :movies
  (fn [db]
    (:movies (:screening db))))

(re-frame/reg-sub
  :presenters
  (fn [db]
    (:presenters (:screening db))))

(re-frame/reg-sub
  :showtimes
  (fn [db]
    (:showtimes (:screening db))))

(re-frame/reg-sub
  :add-new?
  (fn [db [_ category]]
    (:form? (category (:add-new db)))))

(re-frame/reg-sub
  :error
  (fn [db [_]]
    (:error (:screening db))))

(re-frame/reg-sub
  :data
  (fn [db [_ category]]
    (category (:data db))))

(defn add-new-data [db category]
  (into {}
        (for [[k v] (category (:add-new db))]
          [(csk/->camelCase k) v])))

(defn process-showtime [showtime]
  (let [date (at-midnight (:date showtime))
        hour (quot (:time showtime) 100)
        minute (- (:time showtime) (* hour 100))
        showtime (plus date (hours hour) (minutes minute))]
    (print hour)
    (print minute)
    (unparse (formatter-local "yyyy.MM.dd h:mm a") showtime)))


(defn add-screening [db]
  (let [screening (:screening  db)]
    {:movie_id (map :id
                 (:movies screening))
     :presenter_id (remove #(= % {}) (:presenters screening)) 
     :series_id (:series screening)
     :venue_id (:venue screening)
     :showtime (map process-showtime (:showtimes screening))
     :price (:price screening)
     :buy-tickets (:buy-tickets screening)
     :notes (:notes screening)
     :title (:name screening) }))
