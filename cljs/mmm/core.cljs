(ns mmm.core
  (:require
   [mmm.screening :as screening]
   [enfocus.core :as ef]
   [enfocus.events :as events]
   [enfocus.effects :as effects])
  (:require-macros [enfocus.macros :as em]))

(defn setup []
    (do
      (screening/setup)))

(set! (.-onload js/window) setup)