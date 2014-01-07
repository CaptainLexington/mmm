(ns mmm.model.presenter
  (:require [korma.db :as db]
            [korma.core :as korma]
            [mmm.model.db :as local]))

(defn add [name website description]
  (korma/insert local/presenter
                (korma/values {:name name :website website :description description})))

(defn all []
  (korma/select local/presenter
                (korma/order :name :ASC)))