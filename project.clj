(defproject mmm "0.1.0-SNAPSHOT"
  :description "Midnight Movies Mpls app"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [korma "0.3.0-RC5"]
                 [org.postgresql/postgresql "9.2-1002-jdbc4"]
                 [ring "1.2.0"]
                 [compojure "1.1.3"]
                 [hiccup "1.0.2"]
                 [enlive "1.1.4"]
                 [lein-light "0.0.4"]]
  :main mmm.core)
