(ns mmm.model.series
  (:require [korma.db :as db]
            [korma.core :as korma]
            [mmm.model.db :as local]))

(defn add [name website description]
  (korma/insert local/series
                (korma/values {:name name :website website :description description})))

(defn all []
  (korma/select local/series
                (korma/order :name :ASC)))