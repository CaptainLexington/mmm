(defproject mmm "0.9.0"
  :description "Midnight Movies Mpls app"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-cljsbuild "1.0.1"]]
  :dependencies [;;;clj deps
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.namespace "0.2.4"]
                 [org.clojure/tools.reader "0.8.3"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.clojure/core.memoize "0.5.6"]

                 [org.slf4j/slf4j-log4j12 "1.7.1"]
                 [org.eclipse.jetty/jetty-util "7.6.1.v20120215"]
                 [cheshire "5.3.1"]
                 [com.novemberain/monger "2.0.0"]
                 [com.novemberain/validateur "2.2.0"]
                 [lobos "1.0.0-beta1"]
                 [org.postgresql/postgresql "9.2-1002-jdbc4"]
                 [ring "1.2.0"]
                 [com.cemerick/friend "0.2.0"]
                 [compojure "1.1.3"]
                 [org.clojars.ed_sumitra/clojure-webmvc "1.0.0-SNAPSHOT"]
                 [enlive "1.1.4"]
                 [clj-time "0.6.0"]
                 [jarohen/chime "0.1.6"  :exclusions [org.clojure/core.cache]]
                 [twitter-api "0.7.8" :exclusions [org.apache.httpcomponents/httpclient
                                                   org.apache.httpcomponents/httpcore
                                                   slingshot
                                                   commons-codec]]
                 [org.apache.httpcomponents/httpclient "4.3.5"]
                 [shoreleave/shoreleave-remote-ring "0.3.0"]
                 [ithayer/clj-ical "1.2"]
                 [clj-aws-s3 "0.3.7"]
                 ;;;cljs deps
                 [org.clojure/clojurescript "0.0-2120"]
                 [enfocus "2.1.1"]
                 [shoreleave/shoreleave-remote "0.3.0"]
                 [cljs-ajax "0.2.3"]]
  :cljsbuild {
    :builds [{
        ; The path to the top-level ClojureScript source directory:
        :source-paths ["cljs"]
        ; The standard ClojureScript compiler options:
        ; (See the ClojureScript compiler documentation for details.)
        :compiler {
          :output-to "resources/public/scripts/main.js"  ; default: target/cljsbuild-main.js
          :optimizations :none
          :pretty-print true}}]}

  :main mmm.core)
