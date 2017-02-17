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

(defn add-a-movie [movie index]
  [re-com/typeahead
   :data-source search-movies-by-title
   :on-change #(re-frame/dispatch [:update :movies index %])
   :model movie
   :change-on-blur? true
   :debounce-delay 750
   :render-suggestion movie-typeahead-row
   :suggestion-to-string #(:title %)])

(defn nth-item
  [category renderer item index remove?]
  [re-com/h-box
   :children [(renderer item index) 
              [re-com/v-box
               :children [(add-new-input [:add-blank category])
                          (remove-this-input [:remove category index] remove?)]]]])

(defn n-items
  [type renderer items]
  (map-indexed
    #(nth-item type renderer %2 %1 (= (count @items) 1))
    @items))

(defn add-movies [movies add-new-movie]
  [:h2 "Add a New Screening"]
  [:div {:class "movies"}
   [re-com/h-box 
    :children [[:label "Movies"]
               [re-com/md-icon-button
                :md-icon-name (if @add-new-movie
                                "zmdi-minus-circle-outline"
                                "zmdi-plus-circle-o")
                :size :smaller
                :on-click #(re-frame/dispatch [:add-new :movie])]]]
   (when @add-new-movie
     [re-com/v-box
      :children [[:label "Title"]
                 [re-com/input-text
                  :model ""
                  :on-change #(re-frame/dispatch [:add-new-movie-title])]
                 [:label "Director"]
                 [re-com/input-text
                  :model ""
                  :on-change #(re-frame/dispatch [:add-new-movie-director])]
                 [:label "Release Year"]
                 [re-com/input-text
                  :model ""
                  :on-change #(re-frame/dispatch [:add-new-movie-year])]
                 [:label "Running Time"]
                 [re-com/input-text
                  :model ""
                  :on-change #(re-frame/dispatch [:add-new-movie-time])]
                 [:label "Description"]
                 [re-com/input-textarea
                  :model ""
                  :on-change #(re-frame/dispatch [:add-new-movie-description])]
                 [re-com/button 
                  :label "Add"]]])
   (n-items :movies add-a-movie movies)])


(defn add-a-venue []
  [re-com/v-box 
   :children [[:label "Venue"]
              [re-com/single-dropdown
               :choices [{:id 1 :label "Uptown Theater"} {:id 2 :label "Trylon Microcinema"}]
               :model 1
               :on-change #(re-frame/dispatch [:update-venue %])
               :width "250px" ]]])

(defn add-a-series []
  [re-com/v-box 
   :children [[:label "Series"]
              [re-com/single-dropdown
               :choices [{:id 1 :label "Mega Monsoon"} {:id 2 :label "Trying Too Hard"}]
               :model 1
               :on-change #(re-frame/dispatch [:update-series %])
               :width "250px" ]]])

(defn add-a-presenter [presenter index]
  [re-com/single-dropdown
   :choices [{:id 1 :label "Trash Film Debauchery"} {:id 2 :label "Tape Freaks"}]
   :model (:id presenter)
   :on-change #(re-frame/dispatch [:update :presenters index %])
   :width "250px" ])

(defn add-presenters [presenters]
  [re-com/v-box 
   :children [[:label "Presenters"]
              (n-items :presenters add-a-presenter presenters)]])

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
  (let [movies (re-frame/subscribe [:movies])
        add-new-movie (re-frame/subscribe [:add-new :movie])
        presenters (re-frame/subscribe [:presenters])
        ]
    [re-com/v-box
     :children
     [(add-movies movies add-new-movie)
      (add-a-venue)
      (add-a-series)
      (add-presenters presenters)]]))

(defn main-panel []
  (fn []
    [re-com/v-box
     :height "100%"
     :children [(add-screening-form)]]))
