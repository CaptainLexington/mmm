(ns lobos.migrations
  (:refer-clojure :exclude [alter drop
                            bigint boolean char double float time])
  (:use (lobos [migration :only [defmigration]] core schema
               config helpers))
  (:require [clj-time.core :as time]
            [clj-time.coerce :as sqlTime]
            [mmm.model.movie :as movie]
            [mmm.model.venue :as venue]
            [mmm.model.showtime :as showtime]))


(defmigration add-movies-table
  (up [] (create
          (tbl :movies
               (varchar :title 100)
               (varchar :director 50)
               (varchar :year 5)
               (integer :runningTime)
               (varchar :mpaaRating 5)
               (varchar :poster 75)
               (text :description)
               )))
  (down [] (drop (table :posts))))

(defmigration add-venues-table
  (up [] (create
          (tbl :venues
               (varchar :name 50)
               (text :address)
               (text :description)
               (varchar :website 100)
               (varchar :phoneNumber 10)
               )))
  (down [] (drop (table :venues))))

(defmigration add-presenters-table
  (up [] (create
          (tbl :presenters
               (varchar :name 50)
               (varchar :website 100)
               (text :description))))
  (down [] (drop (table :venue))))

(defmigration add-series-table
  (up [] (create
          (tbl :series
               (varchar :name 100)
               (varchar :website 100)
               (text :varchar))))
  (down [] (drop (table :series))))

(defmigration add-events-table
  (up [] (create
          (tbl :events
               (varchar :name 100)
               (varchar :website 100)
               (text :varchar))))
  (down [] (drop (table :series))))

(defmigration add-showtimes-table
  (up [] (create
          (tbl :showtimes
               (date :date)
               (time :time))))
  (down [] (drop (table :events))))


(defmigration add-movies-mock-data
  (up [] (movie/add "Jaws" "Steven Spielberg" 130 "1975" "PG" "/posers/jaws.jpg" "Amity Island Sheriff Martin Brody goes head to head against the Island's powerful tourism board when a shark attack kills a beachgoer late at night."))
  (down [] (movie/delete "Jaws")))

(defmigration add-movies-mock-data-1
  (up [] (do
           (movie/add "Alen" "Ridley Scott" 117 "1979" "R" "/posters/aliens.jpg" "Lt. Ellen Ripley is a commercial spacefarer aboard the commercial mining vessel Nostromo. When they come upon a terrifying, deadly alien creature, Ripley finds herself fighting for her life - and learning who she can trust.")
           (movie/add "Fargo" "Joel Coen" 98 "1996" "R" "/posters/fargo.jpg" "Jerry Lundegard's inept crime falls apart due to his and his henchmen's bundgling and the persistent polce of the very pregnant Marge Gunderson.")
           (movie/add "The Big Lebowski" "Joel Coen" 119 "1998" "R" "/posters/biglebowski.jpg" "\"Dude\" Lebowksi is a middle-aged slacker and league bowler who gets in over his when thugs mistake him for a local millionaire with the same last  name.")
           (movie/add "Casblanca" "Michael Curtiz" 102 "1942" "PG" "/posters/casablanca.jpg" "Often considered the greatest film of all time, Casablanca details the events in occupied French Morocco when a cynical, reclusive former resistence fighter meets his long-lost lover - and her husband, the leader of the international resistence.")))
  (down [] (do
             (movie/delete "Alien")
             (movie/delete "Fargo")
             (movie/delete "The Big Lebowski")
             (movie/delete "Casablanca"))))



(defmigration add-screenings-table
  (up [] (create
          (tbl :screenings
               (text :notes)
               (real :price)
               (refer-to :venues)
               (refer-to :series)
               (refer-to :events))))
  (down [] (drop (table :screenings))))


(defmigration add-join-tables
  (up [] (do
           (create (tbl :movies-screenings
                        (refer-to :movies)
                        (refer-to :screenings)))
           (create (tbl :presenters-screenings
                        (refer-to :presenters)
                        (refer-to :screenings)))))
  (down [] (do
             (drop (table :movies-screenings))
             (drop (table :presenters-screenings)))))


(defmigration update-screenings-with-join-tables
  (up [] (alter :add
                (table :screenings
                       (refer-to :movies-screenings)
                       (refer-to :presenters-screenings))))
  (down [] ))



(defmigration update-venue-phone-numner
  (up [] (do
           (alter :drop
                  (table :venues
                         (column :phoneNumber)))
           (alter :add
                  (table :venues
                         (varchar :phoneNumber 15)))))
  (down []))



(defmigration add-venues-mock-data-1
  (up [] (do
           (venue/add "Uptown Theater" "2906 Hennepin Ave, Minneapolis, MN 55408" "It's the Uptown Theater, ok, so, good" "http://www.landmarktheatres.com/market/minneapolis/uptowntheatre.htm" "(612) 259-0160")
           (venue/add "Lagoon Cinema" "1320 Lagoon Ave, Minneapolis, MN 55408" "It's the Lagoon Cinema, ok, so, good" "http://www.landmarktheatres.com/market/minneapolis/lagooncinema.htm" "612) 823-3020")
           (venue/add "Riverview Theater" "3800 42nd Ave S, Minneapolis, MN 55406" "It's the Riverview Theater, ok, so, good" "riverviewtheater.com" "(612) 729-7369")
           (venue/add "Trylon Microcinema" "3258 Minnehaha Ave, Minneapolis, MN 55406" "It's the Trylon Microcinema, ok, so, good" "take-up.org" "(612) 424-5468")))
  (down [] (do
             (movie/delete 1)
             (movie/delete 2)
             (movie/delete 3)
             (movie/delete 4))))


(defmigration add-showtimes-mock-data
  (up [] (do
           (showtime/add (sqlTime/to-sql-date (time/date-time 2013 11 29)) (sqlTime/to-sql-time (time/date-time 2013 11 29 23 59)))
           (showtime/add (sqlTime/to-sql-date (time/date-time 2013 11 30)) (sqlTime/to-sql-time (time/date-time 2013 11 29 23 59)))
           (showtime/add (sqlTime/to-sql-date (time/date-time 2013 12 01)) (sqlTime/to-sql-time (time/date-time 2013 11 29 23 59)))))
  (down []))


(defmigration add-screenings-to-showtimes
  (up [] (alter :add
                (table :showtimes
                       (refer-to :screenings))))
  (down []))