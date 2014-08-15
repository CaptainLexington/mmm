(ns mmm.view.series
  (:use [net.cgrand.enlive-html])
  (:require [mmm.view.layout :as layout]
            [mmm.model.series :as model]
            [mmm.utils :as utils]
            [clj-time.core :as time]))

(defsnippet all
  (layout/templateLocation "series")
  [:.all]
  [series]
  [:ul :li]
  (clone-for [one-series series]
             [:a]
             (do->
              (set-attr :href (str "/series/edit/" (:_id one-series)))
              (content (:name one-series)))))


