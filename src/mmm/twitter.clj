(ns mmm.twitter
  (:use
    [twitter.oauth]
    [twitter.callbacks]
    [twitter.callbacks.handlers]
    [twitter.api.restful])
  (:require [mmm.config :as config]))

(def my-creds (make-oauth-creds config/twitter-app-consumer-key
                                config/twitter-app-consumer-secret
                                config/twitter-user-access-token
                                config/twitter-user-access-token-secret))


(defn tweet [status]
  (statuses-update :oauth-creds my-creds
                   :params {:status status}))




