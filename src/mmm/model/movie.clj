(ns mmm.model.movie
  (:require [korma.db :as db]
            [korma.core :as korma]
            [mmm.model.db :as local]))

(defn all []
  (korma/select local/movies
    (korma/order :year :ASC)))

(defn add [title director year runningTime releaseYear mpaa poster description]
  (korma/insert local/movies
                (korma/values {:title title :director director :year year :runningTime runningTime :releaseYear releaseYear :mpaa mpaa :poster poster :description description})))