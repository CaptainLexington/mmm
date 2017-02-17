(ns mmm.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]
            [clojure.string :as string]
            [clojure.set :as set]))

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
  :add-new
  (fn [db [_ category]]
    (category (:add-new? db))))
