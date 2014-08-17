(ns mmm.view.movies
  (:use [net.cgrand.enlive-html])
  (:require [mmm.view.layout :as layout]
            [mmm.model.movie :as model]
            [mmm.utils :as utils]
            [clj-time.core :as time]))

(defsnippet all
  (layout/templateLocation "movie")
  [:div.all]
  [movies]
  [:ul :li]
  (clone-for [movie movies]
             [:a]
             (do->
              (set-attr :href (str "/movies/edit/" (:_id movie)))
              (content (str (:title movie)" (" (:year movie)")")))))

(defsnippet edit
  (layout/templateLocation "movie")
  [:div.add.movie]
  [movie]
  [:form]
  (set-attr :action (str "/movies/update/" (:_id movie)))
  [:input.title]
  (set-attr :value (:title movie))
  [:input.director]
  (set-attr :value (:director movie))
  [:input.release-year]
  (set-attr :value (:year movie))
  [:input.running-time]
  (set-attr :value (:runningTime movie))
  [[:option.mpaa (attr= :value (:mpaa-rating movie))]]
  (set-attr :selected "true")
  [:input.poster]
  (set-attr :value (:poster movie))
  [:textarea.description]
  (content (:description movie))
  [:button]
  (content "Save Movie Info"))
