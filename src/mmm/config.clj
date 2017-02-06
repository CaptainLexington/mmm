(ns mmm.config)

(def twitter-app-consumer-key (System/getenv "TWITTER_CONSUMER_KEY"))
(def twitter-app-consumer-secret (System/getenv "TWITTER-CONSUMER-SECRET"))
(def twitter-user-access-token (System/getenv "TWITTER_USER_ACCESS_TOKEN"))
(def twitter-user-access-token-secret (System/getenv "TWITTER_USER_ACCESS_SECRET"))


(def mongo-user (System/getenv "MONGO_USER"))
(def mongo-password (System/getenv "MONGO_PASSWORD"))
(def mongo-url (System/getenv "MONGO_URL"))
(def mongo-db (System/getenv "MONGO_DB"))
