(ns mmm.model.db
  (:require [korma.db :as db]
            [korma.core :as korma]
            [lobos.core :as lbs :only (defcommand migrate)]
            [lobos.migration :as lm]))


(lbs/defcommand pending-migrations []
  (lm/pending-migrations db-spec sname))

(defn actualized?
  "checks in there are no pending migrations"
  []
  (empty? (pending-migrations)))

(def actualize lbs/migrate)


(db/defdb stage (db/postgres {:db "MidnightMoviesLocal"
                        :user "captain"
                        :password ""
                        :port "5432"}))


(declare movie venue presenter series event showtime screening)

(korma/defentity movie
                 (korma/many-to-many screening :movies-screenings))
(korma/defentity venue
                 (korma/has-many screening))
(korma/defentity presenter
                 (korma/has-many screening))
(korma/defentity series
                 (korma/has-many screening))
(korma/defentity event
                 (korma/has-many screening))
(korma/defentity showtime
                 (korma/belongs-to screening))
(korma/defentity screening
                 (korma/belongs-to venue)
                 (korma/belongs-to series)
                 (korma/belongs-to event)
                 (korma/has-many showtime)
                 (korma/many-to-many movie :movies-screenings)
                 (korma/many-to-many presenter :presenters-screenings))
