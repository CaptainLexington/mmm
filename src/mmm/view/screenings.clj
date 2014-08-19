(ns mmm.view.screenings
  (:use [net.cgrand.enlive-html])
  (:require [mmm.view.layout :as layout]
            [mmm.model.movie :as movie]
            [mmm.model.venue :as venue]
            [mmm.model.series :as series]
            [mmm.model.presenter :as presenter]
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
  (if (not= (:title screening) "")
    (content (:title screening))
    (clone-for [i (title-list (:movies screening))]
               (content i)))
  [:div.movie-details :section.movie]
  (clone-for [i (:movies screening)]
             [:img.poster]
             (set-attr :src (:poster i))
             [:p.release]
             (content (:year i))
             [:p.director]
             (content (:director i))
             [:p.rating]
             (content (:mpaa-rating i))
             [:p.runningTime]
             (prepend (str (:runningTime i)))
             [:p.description]
             (content (:description i))
             )
  [:p.presenter :a.presenter]
  (clone-for [presenter (:presenters screening)]
             (do->
              (set-attr :href (str "/presenters/" (:_id presenter)))
              (content (:name presenter))))
  [:p.notes]
  (content (:notes screening))
  [:a.series]
  (do->
   (set-attr :href (str "/series/" (:_id (:series screening))))
   (content (:name (:series screening))))
  [:div.meta.details :div.datetime]
  (clone-for [showtime (arrange-datetime (:showtime screening))]
             [:h3]
             (content (:date showtime))
             [:p]
             (clone-for [time (:time showtime)]
                        (content time)))
  [:div.meta.details :h2.venue :a.venue]
  (do->
   (set-attr :href (str "/venues/" (:_id (:venue screening))))
   (content (:name (:venue screening))))
  [:div.meta.details :p.price]
  (content (utils/display-price (:price screening)))
  [:div.meta.details :p.address]
  (content (:address (:venue screening))))




(defn exclude? [exclude key]
  (some #(= key %) exclude))

(defn display-or-exclude [excludes key]
  (when-not (exclude? excludes key)
    identity))


(defsnippet all
  (layout/templateLocation "screening")
  [:.all]
  [screenings & exclude]
  ;[:th.screening]
  [:th.venue]
  (display-or-exclude exclude :venue)
  [:tr.screening]
  (clone-for [i screenings]
             [:td.screening :p.movie :a.movie]
             (set-attr :href (str "/screenings/" (:_id i)))
             [:td.screening :p.movie]
             (if (not= (:title i) "")
               #(at %
                [:a.movie]
                (content (:title i)))
               (clone-for [j (:movies i)]
                          [:a.movie]
                          (content (:title j))))
             [:td.venue]
             (display-or-exclude exclude :venue)
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
              (set-attr :href (str "/series/" (:_id (:series i)))))
             [:td.edit]
             (display-or-exclude exclude :edit)
             [:td.edit :a]
             (set-attr :href (str "/screenings/edit/" (:_id i)))))


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


; EDIT STUFF


(defn movieSelect [film screening-film]
  (do->
   (set-attr :value (str (:_id film)))
   (set-attr (if (= (:_id film) (:_id screening-film))
               :selected
               :unselected)
             true)
   (content (str (:title film) " (" (:year film) ")"))))


(defn presenterSelect [presenter screening-presenter]
  (do->
   (set-attr :value (str (:_id presenter)))
   (set-attr (if  (= (:_id presenter) (:_id screening-presenter))
               :selected
               :unselected)
             true)
   (content (str (:name presenter) ))))

(defsnippet itemSelect
  (layout/templateLocation "screening")
  [:div.select-snippet :div.singleItem]
  [items item option value]
  [:select]
  (set-attr :name (str value "_id"))
  [:option]
  (clone-for [i items]
             (option i item)))




(defsnippet edit
  (layout/templateLocation "screening")
  [:.add.screening]
  [screening]
  [:form]
  (set-attr :action (str "/screenings/update/" (:_id screening)))
  [:div.prior-films]
  (clone-for [movie (:movies screening)]
             (content (itemSelect (movie/all) movie movieSelect "movie")))
  [:div.prior-presenters]
  (clone-for [presenter (:presenters screening)]
             (content (itemSelect (presenter/all) presenter presenterSelect "presenter")))
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
              (content (str (:name series)))))
  [:div.showtimes.singleItem]
  (clone-for [showtime (:showtime screening)]
             [:input]
             (set-attr :value (utils/write-showtime showtime)))
  [:input.price]
  (set-attr :value (:price screening))
  [:textarea.notes]
  (set-attr :value (:notes screening))
  [:input.title]
  (set-attr :value (:title screening))
  [:button]
  (content "Save Screening Info")
  [:a.delete]
  (set-attr :href (str "/screenings/delete/" (:_id screening))))

