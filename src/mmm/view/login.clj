(ns mmm.view.login
  (:use [net.cgrand.enlive-html])
  (:require [mmm.view.layout :as layout]))

(defsnippet login
  (layout/templateLocation "login")
  [:form]
  []
  )
