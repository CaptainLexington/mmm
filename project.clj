(defproject mmm "0.9.0"
  :description "Midnight Movies Mpls app"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-cljsbuild "1.0.1"]
            [lein-ancient "0.6.8"]
            [lein-ring "0.9.7"]]
  :dependencies [;;;clj deps
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.namespace "0.2.10"]
                 [org.clojure/tools.reader "0.10.0"]
                 [org.clojure/core.memoize "0.5.8"]

                 [cheshire "5.5.0"]
                 [com.novemberain/monger "3.0.2"]
                 [com.novemberain/validateur "2.5.0"]
                 [ring "1.4.0"]
                 [com.cemerick/friend "0.2.1"]
                 [compojure "1.4.0"]
                 [enlive "1.1.6"]
                 [clj-time "0.11.0"]
                 [jarohen/chime "0.1.9"  :exclusions [org.clojure/core.cache]]
                 [clj-oauth "1.5.5"]
                 [twitter-api "0.7.8" :exclusions [clj-oauth
                                                   org.apache.httpcomponents/httpclient
                                                   org.apache.httpcomponents/httpcore
                                                   slingshot
                                                   commons-codec]]
                 [org.apache.httpcomponents/httpclient "4.5.2"]
                 [ithayer/clj-ical "1.2"]
                 ;;;cljs deps
                 [org.clojure/clojurescript "0.0-2120"]
                 [enfocus "2.1.1"]
                 [cljs-ajax "0.5.3"]]
  :cljsbuild {
    :builds [{
        ; The path to the top-level ClojureScript source directory:
        :source-paths ["cljs"]
        ; The standard ClojureScript compiler options:
        ; (See the ClojureScript compiler documentation for details.)
        :compiler {
          :output-to "resources/public/scripts/main.js"  ; default: target/cljsbuild-main.js
          :optimizations :simple
          :pretty-print true}}]}
  :ring {:handler mmm.core/handler}

  :main mmm.core)
