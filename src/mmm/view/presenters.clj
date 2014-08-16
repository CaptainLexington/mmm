(ns mmm.view.presenters
  (:use [net.cgrand.enlive-html])
  (:require [mmm.view.layout :as layout]
            [mmm.view.screenings :as screenings]
            [mmm.model.presenter :as model]
            [mmm.model.screening :as screening]
            [mmm.utils :as utils]
            [clj-time.core :as time]))

(defsnippet all
  (layout/templateLocation "presenters")
  [:.all]
  [presenters]
  [:ul :li]
  (clone-for [presenter presenters]
             [:a]
             (do->
              (set-attr :href  (str "/presenters/edit/" (:_id presenter)))
              (content (:name presenter)))))

(defsnippet view
  (layout/templateLocation "presenters")
  [:.view]
  [presenter]
  [:h2.name]
  (content (:name presenter))
  [:p.website]
  (content (:website presenter))
  [:p.description]
  (content (:description presenter))
  [:div.screenings]
  (content
   (screenings/all
    (screening/current-by-presenter (:_id presenter)))))



(defsnippet edit
  (layout/templateLocation "presenters")
  [:.add]
  [presenter]
  [:form]
  (set-attr :action (str "/presenters/update/" (:_id presenter)))
  [:input.name]
  (set-attr :value (:name presenter))
  [:input.website]
  (set-attr :value (:website presenter))
  [:textarea.description]
  (content (:description presenter))
  [:button]
  (content "Save Presenter Info")
  [:a.delete]
  (set-attr :href (str "/presenters/delete/" (:_id presenter))))

