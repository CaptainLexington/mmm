(ns mmm.view.layout
  (:use [net.cgrand.enlive-html]))


(defn templateLocation
  [file]
  (str "../resources/public/html/" file ".html"))


(deftemplate common
            (templateLocation "layout")
            [body] ;parameter list
            [:main] ;first selector
            (content body)
            )

