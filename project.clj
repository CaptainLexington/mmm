(defproject mmm "0.1.0-SNAPSHOT"
  :description "Midnight Movies Mpls app"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-cljsbuild "1.0.1"]]
  :dependencies [;;;clj deps
                 [org.clojure/clojure "1.5.1"]
                 [org.clojure/tools.namespace "0.2.4"]
                 [org.clojure/tools.reader "0.8.3"]
                 [korma "0.3.0-RC5"]
                 [lobos "1.0.0-beta1"]
                 [org.postgresql/postgresql "9.2-1002-jdbc4"]
                 [ring "1.2.0"]
                 [com.cemerick/friend "0.2.0"]
                 [compojure "1.1.3"]
                 [org.clojars.ed_sumitra/clojure-webmvc "1.0.0-SNAPSHOT"]
                 [enlive "1.1.4"]
                 [clj-time "0.6.0"]
                 [shoreleave/shoreleave-remote-ring "0.3.0"]
                 [clj-aws-s3 "0.3.7"]
                 ;;;cljs deps
                 [org.clojure/clojurescript "0.0-2120"]
                 [enfocus "2.0.2"]
                 [shoreleave/shoreleave-remote "0.3.0"]]
  :cljsbuild {
    :builds [{
        ; The path to the top-level ClojureScript source directory:
        :source-paths ["cljs"]
        ; The standard ClojureScript compiler options:
        ; (See the ClojureScript compiler documentation for details.)
        :compiler {
          :output-to "resources/public/scripts/main.js"  ; default: target/cljsbuild-main.js
          :optimizations :whitespace
          :pretty-print true}}]}

  :main mmm.core)
