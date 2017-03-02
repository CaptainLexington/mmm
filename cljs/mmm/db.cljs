(ns mmm.db
  (:require [clojure.string :as string]
            [ajax.core :refer [POST GET]]
            [re-frame.core :as re-frame]
            [cljs-time.core :as t] ))




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
   :mode :add
   :data {:venues []
          :series []
          :presenters []}
   :add-new {:movies {:form? false}
             :series {:form? false}
             :presenters {:form? false}
             :venues {:form? false}}})


(def blanks
  {:movies {:title ""
            :year ""
            :id ""
            :source "" }
   :presenters 0
   :showtimes {:date (t/now)
               :time 1900 }})
(defn get-all [category]
  (POST
    (str "/" (name category) "/all")
    {:response-format :json
     :keywords? true
     :handler #(re-frame/dispatch [:update [:data category] %])}))

(get-all :presenters)
(get-all :series)
(get-all :venues)
(let [url (.-URL js/document) 
      mode (if (string/includes? url "edit")
             :edit
             :add)
      id (when (= mode :edit)
           (last (string/split url #"/")))]
  (re-frame/dispatch [:update [:mode] mode])
  (when (= mode :edit)
    (re-frame/dispatch [:update [:id] id])
    (re-frame/dispatch [:load-screening id])))
