(ns mmm.handlers
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]
            [mmm.utils :as utils]
            [mmm.subs :as subs]
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

(re-frame/reg-event-fx
  :add-item
  (fn [{:keys [db]} [_ category]]
    {:http-xhrio {:method :post
                  :uri (str "/" (name category) "/add")
                  :params (category (:add-new db))
                  :format (ajax/json-request-format)
                  :response-format (ajax/text-response-format)
                  :on-success [:refresh-data category]
                  :on-failure [:update [:screening :error] "Error!"]}}))

(re-frame/reg-event-fx
  :refresh-data
  (fn [{:keys [db]} [_ category]]
    {:http-xhrio {:method :post
                  :uri (str "/" (name category) "/all")
                  :format (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success [:update [:data category]]
                  :on-failure [:update [:screening :error] "Error!"]}
     :dispatch [:add-new category]}))

(re-frame/reg-event-fx
  :add-screening
  (fn [{:keys [db]} [_ category]]
    {:http-xhrio {:method :post
                  :uri "/screenings/add" 
                  :params (subs/add-screening db)
                  :format (ajax/json-request-format)
                  :response-format (ajax/text-response-format)
                  :on-success []
                  :on-failure [:update [:screening :error] "Error!"]}}))
