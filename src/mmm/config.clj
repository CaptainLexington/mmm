(ns mmm.config)

(def configs (read-string (slurp "config")))

(def twitter-app-consumer-key (:twitter-consumer-key configs))
(def twitter-app-consumer-secret (:twitter-consumer-secret configs))
(def twitter-user-access-token (:twitter-user-access-token configs))
(def twitter-user-access-token-secret (:twitter-user-access-secret configs))


(def mongo-user (:mongo-user configs))
(def mongo-db (:mongo-db configs))