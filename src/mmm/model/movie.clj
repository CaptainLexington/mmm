(ns mmm.model.movie
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.operators :refer :all]
            [mmm.utils :as utils]
            [mmm.moviedb :as mdb]
            [mmm.model.db :as local]))

(defn all []
  (utils/alphabetize-by :title (local/all "movies")))

(defn add [movie-map]
  (mc/insert local/db "movies"
             movie-map))

(defn add-from-movie-db [id]
  (str (:_id
         (mc/insert-and-return local/db "movies"
                               (mdb/cherry-pick-movie
                                 (mdb/movie-by-id id))))))

(defn update [id movie-map]
  (local/updateItemByID
    "movies"
    movie-map
    id))

(defn search-movies-by-title [string]
  (map #(monger.conversion/from-db-object % true) 
       (seq (mc/find local/db "movies"
                     {:title {$regex string
                              $options 'i'
                              }}
                     ["title" "year"]))))

(defn search [string]
  (map #(assoc {} 
               :title  (:title %)
               :year   (:year %)
               :id (str (:_id %)) 
               :source "mmm")
       (search-movies-by-title string)))

(defn getByID [id]
  (local/getItemByID "movies" id))



;; (defn delete [title]
;;   (korma/delete local/movie
;;                 (korma/where (:title [= title]))))
