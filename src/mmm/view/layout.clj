(ns mmm.view.layout
  (:use [net.cgrand.enlive-html]))


(defn templateLocation
  [file]
  (str "../resources/public/html/" file ".html"))


(deftemplate common
  (templateLocation "layout")
  [body & title] ;parameter list
  [:head :title]
  (if-not (nil? title)
    (content title)
    identity)
  [:main] ;first selector
  (content body)
  )

