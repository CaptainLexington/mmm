(ns mmm.scheduled
  (:require [chime :refer [chime-at]]
            [clj-time.core :as time]
            [clj-time.periodic :refer [periodic-seq]]
            [clojure.core.async :as a :refer [<! go-loop]]
            [mmm.model.screening :as screenings]
            [mmm.twitter :as twitter]
            [mmm.utils :as utils])
  (:import [org.joda.time DateTimeZone]))




(defn init-twitter-schedule []
  (chime-at
    (periodic-seq (.. (time/now)
                      (withZone (DateTimeZone/forID "America/Chicago"))
                      (withTime 9 0 0 0))
                  (-> 1 time/days))
    (fn [time]
      (twitter/daily-tweets time))))



(defn init-schedules []
  (init-twitter-schedule))