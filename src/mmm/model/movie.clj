(ns mmm.model.movie
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [mmm.utils :as utils]
            [mmm.model.db :as local]))

(defn all []
  (utils/alphabetize-by :title (local/all "movies")))

(defn add [movie-map]
  (mc/insert local/db "movies"
                movie-map))

(defn update [id movie-map]
  (local/updateItemByID
   "movies"
   movie-map
   id))

(defn getByID [id]
  (local/getItemByID "movies" id))



;; (defn delete [title]
;;   (korma/delete local/movie
;;                 (korma/where (:title [= title]))))
