(ns mmm.view.screenings
  (:use [net.cgrand.enlive-html])
  (:require [mmm.view.layout :as layout]
            [mmm.model.movie :as movie]
            [mmm.model.venue :as venue]
            [mmm.model.series :as series]
            [mmm.utils :as utils]
            [clj-time.core :as time]
            [clj-time.format :as timef]))


(defn split-date-time [showtime]
  [(utils/display-date showtime)
   (utils/display-time showtime)])

(defn arrange-datetime
  [showtimes]
  (let [date-times (map split-date-time showtimes)]
  (sort
   #(compare (first %1) (first %2))
   (mapv
    (fn [[k v]] {:date k :time (flatten (conj [] v))})
    (apply
     merge-with
     #(flatten (conj [%1] %2))
     (mapv (fn [m] {(first m) (second m)})
           date-times))))))


(defn title-list [movies]
  (map (fn [movie] (:title movie)) movies))

(defsnippet view
  (layout/templateLocation "screening")
  [:.view]
  [screening]
  [:div.movie-details :h2 :span.title]
  (clone-for [i (title-list (:movies screening))]
             (content i))
  [:div.movie-details :section.movie]
  (clone-for [i (:movies screening)]
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
  (clone-for [showtime (arrange-datetime (:showtime screening))]
             [:h3]
             (content (:date showtime))
             [:p]
             (clone-for [time (:time showtime)]
                        (content time)))
  [:div.meta.details :h2.venue]
  (content (:name (:venue screening)))
  [:div.meta.details :p.price]
  (content (utils/display-price (:price screening)))
  [:div.meta.details :p.address]
  (content (:address (:venue screening))))

(defsnippet all
  (layout/templateLocation "screening")
  [:.all]
  [screenings]
  [:tr.screening]
  (clone-for [i screenings]
             [:td.screening :p.screening :a.screening]
             (set-attr :href (str "/screenings/" (:_id i)))
             [:td.screening :p.screening :a.screening :span.movie]
             (clone-for [j (:movies i)]
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

