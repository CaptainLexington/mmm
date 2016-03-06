(ns mmm.moviedb
  (:require [cljs.core.async :refer [chan put! take! timeout] :as async]
		    [clojure.walk :refer [prewalk]]
    		[goog.net.XhrIo])
  (:require-macros [cljs.core.async.macros :refer [go]]))


(def api-key "11d1c4855e2fa9dfd3715b87dd5a01f2") 

;;; Same HTTP Examples, but in the browser

(defn fixup-keys [x]
  (prewalk
   (fn [x]
     (if (map? x)
       (zipmap (map keyword (keys x))
               (vals x))
       x))
   x))

;; IO is a tad different in JS, so this function has to be re-written
;; for ClojureScript.
(defn http-get [url]
  (let [c (chan 1)]
    (goog.net.XhrIo/send url (fn [e]
                               (->> e
                                    .-target
                                    .getResponseJson
                                    js->clj
                                    fixup-keys
                                    (put! c))))
    c))

(defn request-and-process [url]
  (go
   (-> (str "http://api.themoviedb.org/3/" url "api_key=" api-key)
       http-get
       <!
       )))

(defn movie-by-id [id]
  (request-and-process (str "movie/" id "?append_to_response=credits&")))

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
         :release-year year
         :running-time running-time
         :poster poster
         :description description}))


(defn fill-in-movie [structure-fn id]
	(go 
    (structure-fn 
      (cherry-pick-movie (<! (movie-by-id id))))))