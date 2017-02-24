(defproject mmm "0.9.0"
  :description "Midnight Movies Mpls app"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-cljsbuild "1.1.2"]
            [lein-ancient "0.6.8"]
            [lein-ring "0.9.7"]]
  :dependencies [;;;clj deps
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.namespace "0.2.10"]
                 [org.clojure/tools.reader "0.10.0"]
                 [org.clojure/core.memoize "0.5.9"]

                 [cheshire "5.7.0"]
                 [com.novemberain/monger "3.1.0" :exclusions  [com.google.guava/guava]]
                 [com.novemberain/validateur "2.5.0"]
                 [ring "1.5.1"]
                 [ring/ring-json "0.4.0"]
                 [clj-http "3.4.1"]
                 [com.cemerick/friend "0.2.3"]
                 [compojure "1.5.2"]
                 [enlive "1.1.6"]
                 [clj-time "0.13.0"]
                 [jarohen/chime "0.2.0"  :exclusions [org.clojure/core.cache]]
                 [clj-oauth "1.5.5"]
                 [twitter-api "0.8.0" :exclusions [clj-oauth
                                                   org.apache.httpcomponents/httpclient
                                                   org.apache.httpcomponents/httpcore
                                                   slingshot
                                                   commons-codec]]
                 [org.apache.httpcomponents/httpclient "4.5.3"]
                 [ithayer/clj-ical "1.2"]
                 [medley "0.8.4"]
                 [clj-fuzzy "0.3.3"]   
                 ;;;cljs deps
                 [org.clojure/clojurescript "1.9.473" :exclusions  [com.google.guava/guava]]
                 [enfocus "2.1.1"]
                 [cljsjs/jquery-ui "1.11.4-0"] 
                 [cljs-ajax "0.5.8"]
                 [reagent "0.6.0-rc" :exclusions  [com.google.guava/guava]]
                 [binaryage/devtools "0.9.1"]
                 [com.google.guava/guava "20.0"]
                 [re-frame "0.9.2" :exclusions  [com.google.guava/guava]]
                 [day8.re-frame/http-fx "0.1.3"]
                 [re-com "2.0.0" :exclusions  [com.google.guava/guava]]
                 [re-frisk "0.3.2"]
                 [com.andrewmcveigh/cljs-time "0.4.0"]
                 [camel-snake-kebab "0.4.0"]]
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
