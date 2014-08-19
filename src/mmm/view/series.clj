(ns mmm.view.series
  (:use [net.cgrand.enlive-html])
  (:require [mmm.view.layout :as layout]
            [mmm.view.screenings :as screenings]
            [mmm.model.series :as model]
            [mmm.model.screening :as screening]
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

(defsnippet view
  (layout/templateLocation "series")
  [:.view]
  [series]
  [:h2.name]
  (content (:name series))
  [:p.website :a]
  (do->
   (set-attr :href (:website series))
   (content "Website"))
  [:p.description]
  (content (:description series))
  [:div.screenings]
  (content
   (screenings/all
    (screening/current-by-series (:_id series))
    :series
    :edit)))

(defsnippet edit
  (layout/templateLocation "series")
  [:.add]
  [series]
  [:form]
  (set-attr :action (str "/series/update/" (:_id series)))
  [:input.name]
  (set-attr :value (:name series))
  [:input.website]
  (set-attr :value (:website series))
  [:textarea.description]
  (content (:description series))
  [:button]
  (content "Save series Info")
  [:a.delete]
  (set-attr :href (str "/series/delete/" (:_id series))))
