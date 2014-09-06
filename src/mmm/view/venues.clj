(ns mmm.view.venues
  (:use [net.cgrand.enlive-html])
  (:require [mmm.view.layout :as layout]
            [mmm.view.screenings :as screenings]
            [mmm.model.screening :as screening]
            [mmm.model.venue :as model]
            [mmm.utils :as utils]
            [clj-time.core :as time]))


(defsnippet all
  (layout/templateLocation "venues")
  [:.all]
  [venues]
  [:ul :li]
  (clone-for [venue venues]
             [:a]
             (do->
              (set-attr :href (str "/venues/edit/" (:_id venue)))
              (content (:name venue)))))

(defsnippet view
  (layout/templateLocation "venues")
  [:.view]
  [venue]
  [:h2.name]
  (content (:name venue))
  [:p.website :a]
  (do->
   (set-attr :href (:website venue))
   (content "Website"))
  [:p.phone-number]
  (content (:phone-number venue))
  [:p.description]
  (content (:description venue))
  [:p.address]
  (content (:address venue))
  [:div.screenings]
  (content
   (screenings/all
    (screening/current-by-venue (:_id venue))
    :venue
    :edit)))

(defsnippet edit
  (layout/templateLocation "venues")
  [:div.add.venue]
  [venue]
  [:form]
  (set-attr :action (str "/venues/update/" (:_id venue)))
  [:input.name]
  (set-attr :value (:name venue))
  [:input.short-name]
  (set-attr :value (:short-name venue))
  [:input.address]
  (set-attr :value (:address venue))
  [:input.website]
  (set-attr :value (:website venue))
  [:input.phone-number]
  (set-attr :value (:phone-number venue))
  [:textarea.description]
  (content (:description venue))
  [:button]
  (content "Save Venue Info")
  [:a.delete]
  (set-attr :href (str "/venues/delete/" (:_id venue))))
