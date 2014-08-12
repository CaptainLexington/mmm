(ns mmm.utils
  (:require [net.cgrand.enlive-html :as html]
            [clojure.java.io :as io])
  (:use [ring.adapter.jetty :only [run-jetty]]
        [ring.util.response :only [response file-response]]
        [ring.middleware.reload :only [wrap-reload]]
        [ring.middleware.file :only [wrap-file]]
        [ring.middleware.stacktrace :only [wrap-stacktrace]]
        [clj-time.core :as time]
        [clj-time.format :as time-fm]
        [clj-time.coerce :as coerce]
        [clj-time.local :as local]))

(def ^:dynamic *webdir* (str (.getCanonicalFile (io/file ".")) "/src/tutorial/"))

(defn render [t]
  (apply str t))

(defn render-snippet [s]
  (apply str (html/emit* s)))

(def render-to-response
  (comp response render))

(defn page-not-found [req]
  {:status 404
   :headers {"Content-type" "text/html"}
   :body "Page Not Found"})

(defn render-request [afn & args]
  (fn [req] (render-to-response (apply afn args))))

(defn serve-file [filename]
  (file-response
   {:root *webdir*
    :index-files? true
    :html-files? true}))

(defn run-server* [app & {:keys [port] :or {port 8080}}]
  (let [nses (if-let [m (meta app)]
               [(-> (:ns (meta app)) str symbol)]
               [])]
    (println "run-server*" nses)
    (run-jetty
     (-> app
         (wrap-file *webdir*)
         (wrap-reload nses)
         (wrap-stacktrace))
     {:port port :join? false})))

(defmacro run-server [app]
  `(run-server* (var ~app)))

(defmulti parse-int type)
(defmethod parse-int java.lang.Integer [n] n)
(defmethod parse-int java.lang.String [s] (Integer/parseInt s))

(defmacro maybe-substitute
  ([expr] `(if-let [x# ~expr] (html/substitute x#) identity))
  ([expr & exprs] `(maybe-substitute (or ~expr ~@exprs))))

(defmacro maybe-content
  ([expr] `(if-let [x# ~expr] (html/content x#) identity))
  ([expr & exprs] `(maybe-content (or ~expr ~@exprs))))

(defn pluralize [astr n]
  (if (= n 1)
    (str astr)
    (str astr "s")))


(defn to-sql-time2
  "Convert `obj` to a java.sql.Time instance."
  [obj]
  (if-let [dt (coerce/to-date-time obj)]
    (java.sql.Time. (.getMillis dt))))

(defn from-sql-time2
  "Returns a DateTime instance in the UTC time zone corresponding to the given
  java.sql.Time object."
  [#^java.sql.Time sql-time]
  (coerce/from-long (.getTime sql-time)))

(defn end-of-this-week
  []
  (.withDayOfWeek (time/today) 7) ;;Returns this Sunday
  )

(defn end-of-next-week
  []
  (.withDayOfWeek (time/plus (time/today) (time/weeks 1)) 7)) ;;Returns next Sunday


(defn two-to-four-weeks-out
  []
  (.withDayOfWeek (time/plus (time/today) (time/weeks 3)) 7)) ;;Returns three Sundays from now



(def am-pm (time-fm/formatter-local "h:mm a"))

(defn local-time [time]
  (time/to-time-zone
    (from-sql-time2 time)
    (time/time-zone-for-offset -6)))

(defn display-date [date]
  (time-fm/unparse
   (time-fm/formatters :date)
   (time/to-time-zone
    (coerce/from-sql-date date)
    (time/time-zone-for-offset -6))))

(defn display-time [time]
  (time-fm/unparse
   am-pm
   (time/to-time-zone
    (from-sql-time2 time)
    (time/time-zone-for-offset -6))))


(defn display-date-and-time [showtime]
  (str (display-date (:date showtime))
       " "
       (display-time (:time showtime))))


(defn display-price [price]
  (str "$" price))
