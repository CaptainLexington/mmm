(ns mmm.view.movies
  (:use [hiccup.core :only (h)]
        [hiccup.form])
  (:require [mmm.view.layout :as layout]))

(defn display-movies [movies]
  (map
     (fn [movie] [:div [:h2 {:class "movie"} (h (:title movie))]
                 [:p {:class "summary"} (h (:description movie))]])
     movies))

(defn movie-form []
  (form-to [:post "movie/add"]
           (label "movie" "Enter a film here!")
           (text-area "title")
           (submit-button "Go!")))


(defn add []
  (layout/common "MMM: Add a film"
                (movie-form)))

(defn index [movies]
  (layout/common "Midnight Movies Minneapolis!"
                 (display-movies movies)))