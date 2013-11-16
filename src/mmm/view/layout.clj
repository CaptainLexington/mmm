(ns mmm.view.layout
  (:use [net.cgrand.enlive-html]))


(defn templateLocation
  [file]
  (str "../resources/public/html/" file ".html"))


(deftemplate common
            (templateLocation "layout")
            [body]
            [:main]
            (content body)
            )

(defsnippet index
            (templateLocation "index")
            [:.index]
            [movies]
            [:.movies-main :.screening]
            (clone-for [i (range (count movies))]
                       identity)
            )

