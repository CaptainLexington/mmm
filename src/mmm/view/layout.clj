(ns mmm.view.layout
  (:use [hiccup.core :only (html)]
        [hiccup.page :only (html5 include-css)]))

(defn common [title & body]
  (html5
    [:head
      [:meta {:charset "utf-8"}]
      [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1, maximum-scale=1"}]
      [:title title]
  (include-css "/stylesheets/mmm.css")]
  [:body
    [:header
      [:h1 {:class "container"} "Midnight Movies Minneapolis"]]
    [:div {:id "content" :class "container"} body]]))


(defn four-oh-four []
  (common "Page Not Found"
    [:div {:id "four-oh-four"}
    "The page you requested could not be found"]))

