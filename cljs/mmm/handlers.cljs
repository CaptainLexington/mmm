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
               [:add-new
                category
                :form?]
               not)))

(re-frame/reg-event-db
  :add-blank
  (fn [db [_ category]]
    (update-in db
               [:screening
                category]
               #(conj
                  % 
                  (category db/blanks)))))

(re-frame/reg-event-db
  :remove
  (fn [db [_ path index]]
    (update-in db
               path 
               #(utils/vec-remove
                  %
                  index))))

(re-frame/reg-event-db
  :update-in
  (fn [db [_ path index new-item]]
    (assoc-in db
             (conj
               path
               index) 
              new-item)))

(re-frame/reg-event-db
  :update
  (fn [db [_ path items]]
   (assoc-in db
             path
             items)))
