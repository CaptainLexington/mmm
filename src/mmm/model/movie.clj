(ns mmm.model.movie
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [mmm.model.db :as local]))

(defn all []
  (sort-by #(clojure.string/lower-case (:title %)) (local/all "movies")))

(defn add [title director runningTime year mpaaRating poster description]
  (mc/insert local/db "movies"
                {:title title :director director :runningTime runningTime :year year :mpaaRating mpaaRating :poster poster :description description}))


(defn getByID [id]
  (local/getItemByID "movies" id))



;; (defn delete [title]
;;   (korma/delete local/movie
;;                 (korma/where (:title [= title]))))
