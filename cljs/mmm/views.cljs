(ns mmm.views
  (:require
   [enfocus.core :as ef]
   [enfocus.events :as events]
   [enfocus.effects :as effects])
  (:require-macros [enfocus.macros :as em]))



(defn movieSelect [film]
  (ef/do->
   (ef/set-attr :value (str (:_id film)))
   (ef/content (str (:title film) " (" (:year film) ")"))
   ))


(defn presenterSelect [presenter]
  (ef/do->
   (ef/set-attr :value (str (:_id presenter)))
   (ef/content (str (:name presenter) ))
   ))

(defn seriesSelect [series]
  (ef/do->
   (ef/set-attr :value (str (:_id series)))
   (ef/content (str (:name series) ))
   ))

(defn venueSelect [venue]
  (ef/do->
   (ef/set-attr :value (str (:_id venue)))
   (ef/content (str (:name venue) ))
   ))

(em/defsnippet showtimesInput
               "/html/screening.html"
               [:div.showtimes :div.singleItem]
               []
  )


(em/defsnippet itemSelect
               "/html/screening.html"
               [:div.select-snippet :div.singleItem]
               [items option value]
               [:select]
               (ef/set-attr :name (str value "_id"))
               ;(ef/add-class value)
               [:option]
               (em/clone-for [item items]
                             (option item))
               )


(em/defsnippet addMovieForm
               "/html/movie.html"
               [:div.add.movie]
               []
               )

(em/defsnippet addPosterForm
               "/html/movie.html"
               [:div.add.poster]
               []
               )

(em/defsnippet addPresenterForm
               "/html/presenters.html"
               [:div.add.presenter]
               []
               )

(em/defsnippet addSeriesForm
               "/html/series.html"
               [:div.add.series]
               []
               )

(em/defsnippet addEventForm
               "/html/events.html"
               [:div.add-event]
               []
               )


(em/defsnippet addVenueForm
               "/html/venues.html"
               [:div.add.venue]
               []
               )
