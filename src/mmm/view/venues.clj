(ns mmm.view.venues
  (:use [net.cgrand.enlive-html])
  (:require [mmm.view.layout :as layout]
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
  [:p.website]
  (content (:website venue))
  [:p.phone-number]
  (content (:phone-number venue))
  [:p.description]
  (content (:description venue))
  [:p.address]
  (content (:address venue))
  [:tr.screening]
  (clone-for [i (screening/getByVenue (:_id venue))]
             [:td.screening :p.screening :a.screening]
             (set-attr :href (str "/screenings/" (:_id i)))
             [:td.screening :p.screening :a.screening :span.movie]
             (clone-for [j (:movies i)]
                        (content (:title j)))
             [:td.price]
             (content (utils/display-price (:price i)))
             [:td.showtimes :p]
             (clone-for [date (utils/date-range (:showtime i))]
                        (content date))
             [:td.presenters :p]
             (clone-for [presenter (:presenters i)]
                        [:a]
                        (do->
                         (set-attr :href (str "/presenters/" (:_id presenter)))
                         (content (:name presenter))))
             [:td.series :p :a]
             (do->
              (content (:name (:series i)))
              (set-attr :href (str "/series/" (:_id (:series i)))))))




(defsnippet edit
  (layout/templateLocation "venues")
  [:div.add.venue]
  [venue]
  [:form]
  (set-attr :action (str "/venues/update/" (:_id venue)))
  [:input.name]
  (set-attr :value (:name venue))
  [:input.address]
  (set-attr :value (:address venue))
  [:input.website]
  (set-attr :value (:website venue))
  [:input.phone-number]
  (set-attr :value (:phone-number venue))
  [:textarea.description]
  (content (:description venue))
  [:button]
  (content "Save Venue Info"))
