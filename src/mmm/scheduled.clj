(ns mmm.scheduled
  (:require [chime :refer [chime-at]]
            [clj-time.core :as time]
            [clj-time.periodic :refer [periodic-seq]]
            [clojure.core.async :as a :refer [<! go-loop]]
            [mmm.model.screening :as screenings]
            [mmm.twitter :as twitter]
            [mmm.utils :as utils])
  (:import [org.joda.time DateTimeZone]))





(defn daily-tweets [time]
  (let [screenings (map identity (screenings/one-day time))]
    (doseq [screening screenings]
      (twitter/tweet (str
                       "TOMORROW: "
                       (:tweet-text screening)
                       "http://www.midnightmoviesmpls.com/screenings/"
                       (:_id screening))))))


(defn init-twitter-schedule []
  (chime-at
    (periodic-seq (.. (time/now)
                      (withZone (DateTimeZone/forID "America/Chicago"))
                      (withTime 9 0 0 0))
                  (-> 1 time/days))
    (fn [time]
      (daily-tweets time))))



(defn init-schedules []
  (init-twitter-schedule))