(ns mmm.screening
  (:require
   [enfocus.core :as ef]
   [enfocus.events :as events]
   [enfocus.effects :as effects]
   [shoreleave.remotes.http-rpc :as rpc])
  (:require-macros [enfocus.macros :as em]
                   [shoreleave.remotes.macros :as slm]))


(defn movieSelect [film]
  (ef/do->
   (ef/content (str (:title film) " (" (:year film) ")"))
   (ef/set-attr :value (str (:id film)))
   ))

(em/defsnippet
 itemSelect
 "/html/screening.html"
 [:div.select-snippet :div.singleItem.film]
 [items option]
 [:option.film]
 (em/clone-for [item items]
               (option item))
 )


(em/defsnippet
 addMovieForm
 "/html/movie.html"
 [:div.add-movie]
 []
 )

(defn addAMovie [mouseEvent]
  (let [submit (.-target mouseEvent)
        movieForm (.-parentElement (.-parentElement submit))
        formData (ef/from movieForm (ef/read-form))]
    (do
      (.preventDefault mouseEvent)
      (rpc/remote-callback
       :addMovie [formData]
       #(ef/at
         [:div.add-movie.inner-form]
         (ef/remove-node))
       ))))

(defn removeSelect [mouseEvent]
  (let [div (.-parentElement (.-target mouseEvent))]
    (ef/at
     div
     (ef/remove-node)))
  )

(em/defaction setup-remotes []
              [:button.add-movie] (events/listen :click addAMovie))

(em/defaction setup-selects []
              [:span.remove] (events/listen :click removeSelect))

(defn loadAddMovieForm []
  (do
    (ef/at [:div.add-movie]
           (ef/content (addMovieForm)))
    (setup-remotes)))

(defn duplicateFormInput [mouseEvent]
  (rpc/remote-callback
   :allMovies
   []
   #(let [plus (.-target mouseEvent)
          item (.-parentElement plus)]
      (do
        (ef/at item (ef/append
                     (itemSelect % movieSelect)
                     ))
        (setup-selects)))))

(em/defaction setup []
              [:.duplicate] (events/listen :click duplicateFormInput)
              [:span.add-movie] (events/listen :click loadAddMovieForm)
              )