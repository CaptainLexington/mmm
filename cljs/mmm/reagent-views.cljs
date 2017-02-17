(ns mmm.reagent-views
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [re-com.core :as re-com]
            [ajax.core :refer [GET POST]]))


(defn search-movies-by-title [query callback]
  (GET
    "/movies/search"
    {:params {:term  query}
     :format :raw
     :response-format :json
     :keywords? true
     :handler callback})
  nil)

(defn movie-typeahead-row [movie]
  (str (:title movie)
       " ("
       (:year movie)
       ") - "
       (:source movie)))

(defn add-new-input [handler]
  [re-com/md-icon-button
   :md-icon-name "zmdi-plus"
   :size :smaller
   :on-click #(re-frame/dispatch handler)])

(defn remove-this-input [handler disabled]
  [re-com/md-icon-button
   :md-icon-name "zmdi-minus"
   :size :smaller
   :disabled? disabled
   :on-click #(re-frame/dispatch handler)])

(defn add-a-movie [index movie]
  [re-com/h-box
   :children [[re-com/typeahead
               :data-source search-movies-by-title
               :on-change #(re-frame/dispatch [:update-movie index %])
               :model movie
               :change-on-blur? true
               :debounce-delay 750
               :render-suggestion movie-typeahead-row
               :suggestion-to-string #(:title %)] 
              [re-com/v-box
               :children [(add-new-input [:add-blank-movie])
                          (remove-this-input [:remove-movie index] (= index 0))]]]])

(defn add-a-venue []
  [re-com/single-dropdown])
(defn add-a-series []
  [re-com/single-dropdown])
(defn add-a-presenter []
  [re-com/single-dropdown])

(defn add-a-showtime []
  [re-com/h-box
   :children [[re-com/datepicker-dropdown
               :on-change #(re-frame/dispatch [:add-date-to-movie])]
              [re-com/input-time
               :model 1930
               :show-icon? true
               :on-change #(re-frame/dispatch [:add-time-to-movie]) ]
              (add-new-input [:add-new-showtime])]])


(defn add-screening-form []
  (let [movies (re-frame/subscribe [:movies])]
    [re-com/v-box
     :children
     [[:h2 "Add a New Screening"]
      [:div {:class "movies"}
       [:label "Movies"]
       (map-indexed
         add-a-movie
         @movies)]]]))

(defn main-panel []
  (fn []
    [re-com/v-box
     :height "100%"
     :children [(add-screening-form)]]))
