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
     #(utils/earliest-first-string (:date %1) (:date %2))
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
             (do->
              (set-attr :href (str "/venues/" (:_id (:venue i))))
              (content (:name (:venue i))))
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


(defsnippet add
  (layout/templateLocation "screening")
  [:.add.screening]
  []
  [:form]
  (set-attr :action "/screenings/add")
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

(defsnippet edit
  (layout/templateLocation "screening")
  [:.add.screening]
  [screening]
  [:form]
  (set-attr :action "/screenings/update")
  [:form :select.venue :option.venue]
  (clone-for [venue (venue/all)]
             (do->
              (set-attr :value (str (:_id venue)))
              (set-attr (if (= (:name venue) (:name (:venue screening)))
                          :selected
                          :unselected)
                        true)
              (content (str (:name venue)))))
  [:form :select.series :option.series]
  (clone-for [series (series/all)]
             (do->
              (set-attr :value (str (:_id series)))
              (set-attr (if (= (:name series) (:name (:series screening)))
                          :selected
                          :unselected)
                        true)
              (content (str (:name series))))))

