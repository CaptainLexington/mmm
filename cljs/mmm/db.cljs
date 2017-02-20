(ns mmm.db
  (:require [ajax.core :refer [POST GET]]
            [re-frame.core :as re-frame]
            [cljs-time.core :as t] 
            ))




(def default-db
  {:name "Midnight Movies Minneapolis"
   :screening {:movies [{}]
               :venue "The Uptown Theater"
               :presenters [{}]
               :series ""
               :showtimes [{:date (t/now) :time 1900}]
               :price "9.00"
               :buy-tickets ""
               :tweet-text ""
               :notes ""
               :name "" }
   :data {:venues []
          :series []
          :presenters []}
   :add-new {:movie {:form? false}
             :series {:form? false}
             :presenter {:form? false}
             :venue {:form? false}}
   })


(def blanks
  {:movies {:title ""
            :year ""
            :id ""
            :source "" }
   :presenters 0
   :showtimes {:date (t/now)
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
