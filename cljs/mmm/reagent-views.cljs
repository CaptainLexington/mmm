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
   :on-change #(re-frame/dispatch [:update-in [:screening  :movies] index %])
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
                          (remove-this-input [:remove [:screening category] index] remove?)]]]])

(defn n-items
  [type renderer items]
  (map-indexed
    #(nth-item type renderer %2 %1 (= (count @items) 1))
    @items))

(defn add-new-item-input [category field]
  [re-com/v-box
   :children [[:label (name field)]
              [(if (= field :description)
                 re-com/input-textarea
                 re-com/input-text)
               :model ""
               :on-change #(re-frame/dispatch [:update [:add-new category field] %])]]])

(defn add-new-item [enabled? category fields]
  [re-com/v-box 
   :children
   [[re-com/h-box 
     :children [[:label (name category)]
                [re-com/md-icon-button
                 :md-icon-name (if @enabled?
                                 "zmdi-minus-circle-outline"
                                 "zmdi-plus-circle-o")
                 :size :smaller
                 :on-click #(re-frame/dispatch [:add-new category])]]]
    (when @enabled?
      [re-com/v-box
       :children [[:div {:class "add-new"}
                   (map (partial add-new-item-input category)
                        fields)
                   [re-com/button
                    :label "Add"
                    :on-click #(re-frame/dispatch [:add-item category])]]]])]])

(defn add-movies [movies add-new-movie]
  [:h2 "Add a New Screening"]
  [:div {:class "movies"}
   (add-new-item
     add-new-movie
     :movies 
     [:title :director :running-time :release-year :poster-url :description])
   (n-items :movies add-a-movie movies)])


(defn add-a-venue [venue-data add-new-venue]
  [re-com/v-box 
   :children [(add-new-item
                add-new-venue
                :venues
                [:name :short-name :address :website :description])
              [re-com/single-dropdown
               :choices venue-data 
               :model nil
               :id-fn :_id
               :label-fn :name
               :on-change #(re-frame/dispatch [:update [:screening :venue] %])
               :width "250px" ]]])

(defn add-a-series [series-data add-new-series]
  [re-com/v-box 
   :children [(add-new-item
                add-new-series
                :series
                [:name :website :description])
              [re-com/single-dropdown
               :choices series-data 
               :model nil
               :id-fn :_id
               :label-fn :name
               :on-change #(re-frame/dispatch [:update [:screening :series] %])
               :width "250px" ]]])

(defn add-a-presenter [presenter-data presenter index]
  [re-com/single-dropdown
   :choices presenter-data 
   :model presenter 
   :id-fn :_id
   :label-fn :name
   :on-change #(re-frame/dispatch [:update-in [:screening :presenters] index %])
   :width "250px" ])

(defn add-presenters [presenters presenter-data add-new-presenter]
  [re-com/v-box 
   :children [(add-new-item
                add-new-presenter
                :presenters
                [:name :website :description])
              (n-items :presenters (partial add-a-presenter presenter-data) presenters)]])


(defn add-a-showtime [showtime index]
  [re-com/h-box
   :children [[re-com/datepicker-dropdown
               :model (:date showtime)
               :show-today? true
               :on-change #(re-frame/dispatch [:update [:screening :showtimes index :date] %])]
              [re-com/input-time
               :model (:time showtime)
               :show-icon? true
               :on-change #(re-frame/dispatch [:update [:screening :showtimes index :time] %])]]])

(defn add-showtimes [showtimes]
  [re-com/v-box
   :children [[:label "Showtimes"]
              (n-items :showtimes add-a-showtime showtimes)]])

(defn misc-info []
  [re-com/v-box
   :children [[:label "Price"]
              [re-com/input-text
               :model ""
               :on-change #(re-frame/dispatch [:update [:screening :price] %])]
              [:label "Buy Tickets"]
              [re-com/input-text
               :model ""
               :on-change #(re-frame/dispatch [ :update [:screening :buy-tickets] %] )]
              [:label "Notes"]
              [re-com/input-textarea
               :model ""
               :on-change #(re-frame/dispatch [ :update [:screening :notes] %])]
              [:label "Name"]
              [re-com/input-text
               :model ""
               :on-change #(re-frame/dispatch [ :update [:screening :name] %])]]])

(defn add-screening-form []
  (let [movies (re-frame/subscribe [:movies])
        add-new-movie (re-frame/subscribe [:add-new? :movies])
        add-new-venue (re-frame/subscribe [:add-new? :venues])
        add-new-series (re-frame/subscribe [:add-new? :series])
        add-new-presenter (re-frame/subscribe [:add-new? :presenters])
        presenters (re-frame/subscribe [:presenters])
        showtimes (re-frame/subscribe [:showtimes])
        presenter-data (re-frame/subscribe [:data :presenters])
        series-data (re-frame/subscribe [:data :series])
        venue-data (re-frame/subscribe [:data :venues]) 
        error (re-frame/subscribe [:error])
        mode (re-frame/subscribe [:mode])]
    [re-com/v-box
     :children
     [(add-movies movies add-new-movie)
      (add-a-venue venue-data add-new-venue)
      (add-a-series series-data add-new-series)
      (add-presenters presenters presenter-data add-new-presenter)
      (add-showtimes showtimes)
      (misc-info)
      (when @error
        [:p {:class "error"} @error])
      [re-com/button
       :label "Add Screening"
       :on-click #(re-frame/dispatch [:submit-screening @mode])]]]))

(defn main-panel []
  (fn []
    [re-com/v-box
     :height "100%"
     :children [(add-screening-form)]]))
