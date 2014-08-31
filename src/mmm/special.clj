(ns mmm.special
  (:use [compojure.core :only (defroutes GET)]
        [ring.middleware params
         keyword-params
         session]
        [ring.util.response :as response]
        [ring.adapter.jetty :as ring]
        [mmm.utils])
  (:require [mmm.controller.venue :as venue]
            [mmm.controller.presenter :as presenter]))


(defroutes routes
  (GET "/tfd" [] (render-request presenter/view "53fd0be4e4b0e4e0e6fe0d97"))
  (GET "/takeup" [] (render-request presenter/view "53f163eee4b0ae4a3f2c948a"))
  (GET "/uptown" [] (render-request venue/view "53f15b8ce4b0ae4a3f2c9486"))
  (GET "/moa" [] (render-request venue/view "53f1690ce4b0ae4a3f2c9490"))
  (GET "/heights" [] (render-request venue/view "53f1fccbe4b045b8715a5993"))
  (GET "/trylon" [] (render-request venue/view "53f163c7e4b0ae4a3f2c9489")))
