(ns lobos.config
  (:use lobos.connectivity))


(def db
  {:classname "org.postgresql.Driver"
   :subprotocol "postgresql"
   :user "captain"
   :password ""
   :subname "//localhost:5432/MidnightMoviesLocal"})


(open-global db)