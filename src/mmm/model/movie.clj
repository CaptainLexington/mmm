(ns mmm.model.movie
  (:require [korma.db :as db]
            [korma.core :as korma]
            [mmm.model.db :as local]))

(defn all []
  (korma/select local/movie
    (korma/order :title :ASC)))

(defn add [title director runningTime year mpaaRating poster description]
  (korma/insert local/movie
                (korma/values {:title title :director director :runningTime runningTime :year year :mpaaRating mpaaRating :poster poster :description description})))


(defn delete [title]
  (korma/delete local/movie
                (korma/where (:title [= title]))))
