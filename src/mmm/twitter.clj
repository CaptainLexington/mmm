(ns mmm.twitter
  (:use
    [twitter.oauth]
    [twitter.callbacks]
    [twitter.callbacks.handlers]
    [twitter.api.restful])
  (:require [mmm.config :as config]
            [mmm.model.screening :as screenings]))

(def my-creds (make-oauth-creds config/twitter-app-consumer-key
                                config/twitter-app-consumer-secret
                                config/twitter-user-access-token
                                config/twitter-user-access-token-secret))


(defn tweet [status]
  (statuses-update :oauth-creds my-creds
                   :params {:status status}))

(defn daily-tweets [time]
  (let [screenings (map identity (screenings/one-day time))]
    (doseq [screening screenings]
      (tweet (str
               "TONIGHT: "
               (:tweet-text screening)
               "http://www.midnightmoviesmpls.com/screenings/"
               (:_id screening))))))

