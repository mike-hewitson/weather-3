(ns weather-3.test.db.core
  (:require [ring.mock.request :refer :all]
            [weather-3.db.core :refer :all]
            [weather-3.db-init :as init]
            [weather-3.test.fixtures :as fix]
            [midje.sweet :refer :all]))


; TODO complete test
; this is currently hard-coded to prvent nasty things hapenning
(def uri "datomic:free://localhost:4334/weather_3_test")

(defn initialize-db
  "initialise a database reading for testing"
  [uri]
  (do
   (init/delete-database uri)
   (init/create-database uri)
   (init/add-schema uri)
   (init/add-locations uri)))

(future-facts "about 'get-reading-at-a-time'"
  (with-state-change [(before :contents) (initialize-db uri)]
;   (let [reading (get-darksky-data "51.317,0.057")]
    (fact "given nothing it should return a map"
      (map? (get-reading-at-a-time)) => true)))
;     (fact "it should return the right data"
;       (map? (:currently reading)) => true)
;     (fact "it should return all of the correct sections"
;       (map? (:currently reading)) => true)
;     (map? (:daily reading)) => true))

; (facts "about 'extract-reading-data'"
;   (let [reading-data (extract-reading-data fix/a-darksky-reading-body)]
;     (fact "it should return a map"
;      (map? reading-data) => true)
;     (fact "it should return the correct data"
;      (get reading-data ["day-summary"]) => "Foggy in the evening."
;      (get reading-data ["now-summary"]) => "Partly Cloudy")))
;
; (facts "about 'create-update'"
;  (let [update (create-update "London"
;                              (extract-reading-data fix/a-darksky-reading-body))]
;   (fact "it should return a sequence"
;     (seq? update) => true)
;   (fact "it should contain 14 items"
;     (count update) => 14)
;   (fact "it should contain some correct data"
;     (first update) => {:readings/wind-bearing 31, :db/id [:location/name "London"]}
;     (last update) => {:db/id [:location/name "London"], :readings/precip-intensity 0.0})))
;
; (facts "about 'log-one-reading'"
;   (let [result (->> (get-darksky-data "51.317,0.057")
;                     (extract-reading-data)
;                     (create-update "London")
;                     (log-one-reading conn))]
;     (fact "it should return a map"
;       (map? result) => true)
;     (fact "results should conain valid data"
;       (type (result :db-before)) => datomic.db.Db
;       (type (result :db-after)) => datomic.db.Db
;       (nil? (result :tx-data)) => false)))
