(ns mmm.handlers
  (:require [re-frame.core :as re-frame]
            [mmm.utils :as utils]
            [mmm.db :as db]))

(re-frame/reg-event-db
  :initialize-db
  (fn  [_ _]
    db/default-db))

(re-frame/reg-event-db 
  :add-new
  (fn [db [_ category]]
    (update-in db
               [:add-new?
                category]
               not)))

(re-frame/reg-event-db
  :add-blank
  (fn [db [_ category]]
    (update-in db
               [:screening
                category]
               #(conj
                  % 
                  {}))))

(re-frame/reg-event-db
  :remove
  (fn [db [_ category index]]
    (update-in db
               [:screening
                category]
               #(utils/vec-remove
                  %
                  index))))

(re-frame/reg-event-db
  :update
  (fn [db [_ category index new-item]]
    (assoc-in db
              [:screening
               category
               index]
              new-item)))
