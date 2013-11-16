(ns mmm.view.index
  (:use [net.cgrand.enlive-html])
  (:require [mmm.view.layout :as layout]))


(defn show-movies-for-week [screenings]
  (clone-for [i screenings]
             [:h3.venue]
             (content (:name i))))

(defsnippet index
  (layout/templateLocation "index")
  [:.index]
  [screenings]
  [:.movies-main :.this-week :.screening]
  (show-movies-for-week screenings)
  [:.movies-main :.next-week :.screening]
  (show-movies-for-week (rest screenings))
  [:.movies-main :.coming-soon :.screening]
  (show-movies-for-week (rest (rest screenings)))
  )
