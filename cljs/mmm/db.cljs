(ns mmm.db
  (:require [ajax.core :refer [POST GET]]
            [re-frame.core :as re-frame]))

(def default-db
  {:name "Midnight Movies Minneapolis"
  :screening {:movies [{:title "Jaws"
                        :id "556cf5bae4b0b96d5595517c"
                        }
                       {:title "Jurassic Park"
                        :id "573322a9d6f78e000300757e"
                        }
                       ]
              :venue "The Uptown Theater"
              :presenters ["Indeed Brewing"]
              :series ""
              :showtimes [{:date "2017.04.01", :time "19:00"}]
              :price "9.00"
              :buy-tickets ""
              :tweet-text ""
              :notes ""
              :name "" }})
