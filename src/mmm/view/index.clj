(ns mmm.view.index
  (:use [net.cgrand.enlive-html]
        [clj-time.format]
        [clj-time.coerce])
  (:require [mmm.view.layout :as layout]))


(defn display-time [screening]
  (:created_on (first (:showtime screening))))



(defn show-movies-for-week [screenings]
  (clone-for [i screenings]
             [:h3.venue]
             (content (:name i))
             [:img.poster.main_page]
             (clone-for [j (:movie i)]
                        (set-attr :src (:poster j)))
             [:a]
             (set-attr :href (str "/screenings/" (:id i)))
            ; [:span.datetime]
            ; (content (display-time i))
             ))

(defsnippet index
  (layout/templateLocation "index")
  [:.index]
  [screenings]
  [:.movies-main :.week-container]
  (clone-for [i ["THIS WEEK", "NEXT WEEK", "COMING SOON"]]
             [:h2.list-label]
             (content i)
             [:.screening]
             (show-movies-for-week screenings))
  )


(defsnippet about
  (layout/templateLocation "index")
  [:.about]
  []
  )

