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
  [venues]
  [:tr.screening]
  (let [venue (first venues)]
  (clone-for [i (screening/getByVenue (:id venue))]
             [:td.screening :p.screening :a.screening]
             (set-attr :href (str "/screenings/" (:id i)))
             [:td.screening :p.screening :a.screening :span.movie]
             (clone-for [j (:movie i)]
                        (content (:title j)))
             [:td.price]
             (content (utils/display-price (:price i)))
             [:td.showtimes :p]
             (clone-for [showtime (:showtime i)]
                        (content (utils/display-date-and-time showtime)))
             )
  ))