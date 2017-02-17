(ns mmm.db
  (:require [ajax.core :refer [POST GET]]
            [re-frame.core :as re-frame]))

(def default-db
  {:name "Midnight Movies Minneapolis"
  :screening {:movies [{}]
              :add-new? {:movie false
                         :series false
                         :presenter false
                         :venue false }
              :venue "The Uptown Theater"
              :presenters [{}]
              :series ""
              :showtimes [{:date "2017.04.01", :time "19:00"}]
              :price "9.00"
              :buy-tickets ""
              :tweet-text ""
              :notes ""
              :name "" }})