;   Copyright (c) Cognitect, Inc. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.
(ns weather-3.log-data
  (:require [datomic.api :as d]
            [clj-http.client :as client]
            [clojure.tools.logging :as log]
            ; [weather-3.config :refer [env]]
            ; [weather-3.db.core :as db]
            [cprop.core :refer [load-config]]
            [cprop.source :as source]))
            ; [weather-3.db.core :refer [conn]]))

; TODO add doc strings

(def reading-names
  [[["day-summary"]
    ["sunrise"]
    ["sunset"]
    ["summary"]
    ["icon"]
    ["temperature" float]
    ["wind-speed" float]
    ["wind-bearing" long]
    ["pressure" float]
    ["humidity" float]
    ["precip-probability" float]
    ["precip-intensity" float]
    ["cloud-cover" float]]
   [[:daily :summary]
    [:data :sunsetTime]
    [:data :sunriseTime]
    [:currently :summary]
    [:currently :icon]
    [:currently :temperature]
    [:currently :windSpeed]
    [:currently :windBearing]
    [:currently :pressure]
    [:currently :humidity]
    [:currently :precipProbability]
    [:currently :precipIntensity]
    [:currently :cloudCover]]])

(def locations
  [["London" "51.317,0.057"]
   ["Sandton" "-26.097,28.053"]
   ["Paradise Beach" "-34.089,24.903"]])

; TODO split out the data fetch

(defn get-data [gps]
  (let [my-url (str
                 "https://api.darksky.net/forecast/62888a9ff1907377b60a866701cf3338/"
                 gps)
        darksky-data (client/get my-url {:as :json})
        body (:body darksky-data)]
    (map (fn [[k v]]
           (cond
             (= k :daily) (v (:daily body))
             (= k :data) (java.util.Date. (* 1000 (v (first (:data (:daily body))))))
             (= k :currently) (v (:currently body))
             :else nil))
         (last reading-names))))

(defn create-update [location gps]
  (map (fn [[[name & [cast]] value]] {:db/id [:location/name location] (keyword (str "readings/" name))
                                      (if cast (cast value) value)})
       (zipmap (first reading-names) (get-data gps))))

(defn readings-data []
  (map (fn [[name gps]] (create-update name gps)) locations))

(def conn
  (let [uri (:database-url
              (load-config :merge
                [(source/from-system-props)
                 (source/from-env)]))]
    (d/connect uri)))

(defn log-one-reading [reading]
  @(d/transact conn reading))

(defn log-readings []
  (let [data (readings-data)]
    (log/debug "the results :" (map #(log-one-reading %) data))))


; TODO move history enquiry to code.db

;; but everything ever said is still there
; (def history (d/history (d/db conn)))
; (require '[clojure.pprint :as pp])
; (->> (d/q '[:find ?e ?a ?v ?tx ?op
;             :in $
;             :where [?e :location/name "London"]
;             [?e ?a ?v ?tx ?op]]
;           history)
;      (map #(zipmap [:e :a :v :tx :op] %))
;      (sort-by :tx)
;      (pp/print-table [:e :a :v :tx :op]))

(defn -main [& args]
  (log-readings)
  (log/info "Logged one set of readings")
  (System/exit 0))
