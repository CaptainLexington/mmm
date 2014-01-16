(ns mmm.view.index
  (:use [net.cgrand.enlive-html])
  (:require [mmm.view.layout :as layout]
            [mmm.model.screening :as screening]
            [mmm.utils :as utils]))



(defn time-list [screening]
  (let [showtimes (:showtime screening)]
    (if (= (count showtimes) 1)
      showtimes
      (list (first showtimes) (last showtimes))
      )))



(defn show-movies-for-week [screenings]
  (clone-for [i screenings]
             [:h3.venue]
             (content (:name i))
             [:img.poster.main_page]
             (clone-for [j (:movie i)]
                        (set-attr :src (:poster j)))
             [:a]
             (set-attr :href (str "/screenings/" (:id i)))
             [:span.datetime]
             (clone-for [j (time-list i)]
                        (content (utils/display-date-and-time j)))
             ))


(defn screeningsForHomePage []
  (let [this-week (screening/thisWeek)
        next-week (screening/nextWeek)
        coming-soon (screening/comingSoon)
        full-page [["THIS WEEK", this-week],
                   ["NEXT WEEK", next-week],
                   ["COMING SOON", coming-soon]]]

    (filter #(not (empty? (last %)))
            full-page
            )))

(defsnippet index
  (layout/templateLocation "index")
  [:.index]
  [screenings]
  [:.movies-main :.week-container]
  (clone-for [i (screeningsForHomePage)]
             [:h2.list-label]
             (content (first i))
             [:.screening]
             (show-movies-for-week (last i)))
  )


(defsnippet about
  (layout/templateLocation "index")
  [:.about]
  []
  )

