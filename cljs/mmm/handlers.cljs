(ns mmm.handlers
  (:require [re-frame.core :as re-frame]
            [mmm.utils :as utils]
            [mmm.db :as db]))

(re-frame/reg-event-db
  :initialize-db
  (fn  [_ _]
    db/default-db))

(re-frame/reg-event-db
  :add-blank-movie
  (fn [db [_ index new-movie]]
    (update-in db
               [:screening
                :movies ]
               #(conj
                  % 
                  {:title ""
                   :id ""}))))

(re-frame/reg-event-db
  :remove-movie
  (fn [db [_ index]]
    (update-in db
               [:screening
                :movies]
               #(utils/vec-remove
                  %
                  index))))

(re-frame/reg-event-db
  :update-movie
  (fn [db [_ index new-movie]]
    (assoc-in db
              [:screening
               :movies
               index ]
              {:title (:title new-movie)
               :id (:id new-movie)})))
