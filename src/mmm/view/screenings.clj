(ns mmm.view.screenings
  (:use [net.cgrand.enlive-html])
  (:require [mmm.view.layout :as layout]
            [mmm.model.movie :as movie]
            [mmm.model.venue :as venue]
            [mmm.model.series :as series]
            [mmm.utils :as utils]
            [clj-time.core :as time]))



(defn arrange-datetime
  [showtimes]
  (sort
   #(compare (:date %1) (:date %2))
   (mapv
    (fn [[k v]] {:date k :time (flatten (conj [] v))})
    (apply
     merge-with
     #(flatten (conj [%1] %2))
     (mapv (fn [m] {(:date m) (:time m)})
           showtimes)))))



(defn title-list [movies]
  (map (fn [movie] (:title movie)) movies))

(defsnippet view
  (layout/templateLocation "screening")
  [:.view]
  [screening]
  [:div.movie-details :h2 :span.title]
  (clone-for [i (title-list (:movie (first screening)))]
             (content i))
  [:div.movie-details :section.movie]
  (clone-for [i (:movie (first screening))]
             [:img.poster]
             (set-attr :src (:poster i))
             [:p.release]
             (content (:releaseYear i))
             [:p.director]
             (content (:director i))
             [:p.rating]
             (content (:mpaaRating i))
             [:p.runningTime]
             (prepend (str (:runningTime i)))
             [:p.description]
             (content (:description i))
             )
  [:div.meta.details :div.datetime]
  (clone-for [showtime (arrange-datetime (:showtime (first screening)))]
             [:h3]
             (content (utils/display-date (:date showtime)))
             [:p]
             (clone-for [time (:time showtime)]
                        (content (utils/display-time time)))
             )
  [:div.meta.details :h2.venue]
  (content (:name (first screening)))
  [:div.meta.details :p.price]
  (content (utils/display-price (:price (first screening))))
  [:div.meta.details :p.address]
  (content (:address (first screening))))

(defsnippet all
  (layout/templateLocation "screening")
  [:.all]
  [screenings]
  [:tr.screening]
  (clone-for [i screenings]
             [:td.screening :p.screening :a.screening]
             (set-attr :href (str "/screenings/" (:id i)))
             [:td.screening :p.screening :a.screening :span.movie]
             (clone-for [j (:movie i)]
                        (content (:title j)))
             [:td.venue :p.venue :a.venue]
             (content (:name (:venue i)))
             [:td.price]
             (content (utils/display-price (:price i)))
             [:td.showtimes :p]
             (clone-for [showtime (:showtime i)]
                        (content (utils/display-date-and-time showtime)))
             ))


(defsnippet add
  (layout/templateLocation "screening")
  [:.add.screening]
  []
  [:form :select.venue :option.venue]
  (clone-for [venue (venue/all)]
             (do->
              (set-attr :value (str (:_id venue)))
              (content (str (:name venue)))))
  [:form :select.series :option.series]
  (clone-for [series (series/all)]
             (do->
              (set-attr :value (str (:_id series)))
              (content (str (:name series))))))

