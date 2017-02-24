(ns mmm.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]
            [clojure.string :as string]
            [clojure.set :as set]
            [camel-snake-kebab.core :as csk]))

(re-frame/reg-sub
  :name
  (fn [db]
    (:name db)))

(re-frame/reg-sub
  :movies
  (fn [db]
    (:movies (:screening db))))

(re-frame/reg-sub
  :presenters
  (fn [db]
    (:presenters (:screening db))))

(re-frame/reg-sub
  :showtimes
  (fn [db]
    (:showtimes (:screening db))))

(re-frame/reg-sub
  :add-new?
  (fn [db [_ category]]
    (:form? (category (:add-new db)))))

(re-frame/reg-sub
  :error
  (fn [db [_]]
    (:error (:screening db))))

(re-frame/reg-sub
  :data
  (fn [db [_ category]]
    (category (:data db))))

(defn add-new-data [db category]
  (into {}
        (for [[k v] (category (:add-new db))]
          [(csk/->camelCase k) v])))
