(ns mmm.moviedb
  (:require [clj-http.client :as client]))

(def api-key "11d1c4855e2fa9dfd3715b87dd5a01f2") 


(defn- request [url]
  (client/get (str "https://api.themoviedb.org/3/" url "api_key=" api-key) {:as :json}))

(defn movie-by-id [id]
  (:body (request (str "movie/" id "?append_to_response=credits&"))))

(defn search-movies-by-string [string]
  (request (str "search/movie?query=" string "&")))

(defn process-movie [movie]
  (let [release-year (if (empty? (:release_date movie))
                       "????"
                       (subs (:release_date movie) 0 4))]
    {:title (:title movie)
     :year release-year 
     :id (:id movie)}))

(defn list-of-searched-movies [search-results]
  (map process-movie 
       (:results (:body search-results))))

(defn search [string]
  (map #(assoc % :source "mdb")
       (list-of-searched-movies
         (search-movies-by-string string))))

(defn full-poster-path [poster]
  (str "https://image.tmdb.org/t/p/w185" poster))

(defn get-director [movie-crew]
  (first (filter #(= (:job %) "Director") movie-crew)))

(defn cherry-pick-movie [movie]
  (let [title (:title movie)
        director (:name  (get-director (:crew (:credits movie))))
        year (apply str (take 4 (:release_date movie)))
        running-time (:runtime movie)
        poster (full-poster-path (:poster_path movie))
        description (:overview movie)]
    {:title title
     :director director
     :year year
     :runningTime running-time
     :poster poster
     :description description}))


