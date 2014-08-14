(ns mmm.view.venues
  (:use [net.cgrand.enlive-html])
  (:require [mmm.view.layout :as layout]
            [mmm.model.venue :as model]
            [mmm.model.movie :as movie]
            [mmm.model.screening :as screening]
            [mmm.model.series :as series]
            [mmm.utils :as utils]
            [clj-time.core :as time]))

(defsnippet view
  (layout/templateLocation "venues")
  [:.view]
  [venue]
  [:h2.name]
  (content (:name venue))
  [:p.website]
  (content (:website venue))
  [:p.phone-number]
  (content (:phoneNumber venue))
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
                        (content date))))
