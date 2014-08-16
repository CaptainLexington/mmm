(ns mmm.view.admin
  (:use [net.cgrand.enlive-html])
  (:require [mmm.view.layout :as layout]
            [mmm.model.screening :as screening]
            [mmm.utils :as utils]))

(defsnippet admin
  (layout/templateLocation "admin")
  [:.index]
  []
  )
