(ns mmm.model.db
  (:require [korma.db :as db]
            [korma.core :as korma]))


(db/defdb stage (db/postgres {:db "MidnightMoviesLocal"
                        :user "captain"
                        :password ""
                        :port "5432"}))

(korma/defentity movies
                 (korma/entity-fields :title :director :year :runningTime :releaseYear :mpaa :poster :description))
