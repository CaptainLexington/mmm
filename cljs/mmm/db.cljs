(ns mmm.db
  (:require [ajax.core :refer [POST GET]]
            [re-frame.core :as re-frame]))




(def default-db
  {:name "Midnight Movies Minneapolis"
   :screening {:movies [{}]
               :venue "The Uptown Theater"
               :presenters [{}]
               :series ""
               :showtimes [{:date "2017.04.01", :time 1900}]
               :price "9.00"
               :buy-tickets ""
               :tweet-text ""
               :notes ""
               :name "" }
   :data {:venues []
          :series []
          :presenters []}
   :add-new? {:movie false
              :series false
              :presenter false
              :venue false }
   })


(def blanks
  {:movies {:title ""
            :year ""
            :id ""
            :source "" }
   :presenters 0
   :showtimes {:date "2017.02.20"
               :time 1900 }})

(POST
  "/presenters/all"
  {:response-format :json
   :keywords? true
   :handler #(re-frame/dispatch [:update [:data :presenters] %])})
(POST
  "/series/all"
  {:response-format :json
   :keywords? true
   :handler #(re-frame/dispatch [:update [:data :series] %])})
(POST
  "/venues/all"
  {:response-format :json
   :keywords? true
   :handler #(re-frame/dispatch [:update [:data :venues] %])})
