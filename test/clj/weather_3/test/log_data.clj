(ns weather-3.test.log-data
  (:require [ring.mock.request :refer :all]
            [weather-3.log-data :refer :all]
            [midje.sweet :refer :all]
            [weather-3.test.fixtures :as fix]))

(facts "about 'get-darksky-reading'"
  (let [reading (get-darksky-data "51.317,0.057")]
    (fact "it should return a map"
      (map? reading) => true)
    (fact "it should return the right data"
      (map? (:currently reading)) => true)
    (fact "it should return all of the correct sections"
      (map? (:currently reading)) => true)
    (map? (:daily reading)) => true))

(facts "about 'extract-reading-data'"
  (let [reading-data (extract-reading-data fix/a-darksky-reading-body)]
    (fact "it should return a map"
     (map? reading-data) => true)
    (fact "it should return the correct data"
     (get reading-data ["day-summary"]) => "Foggy in the evening."
     (get reading-data ["now-summary"]) => "Partly Cloudy")))

(facts "about 'create-update'"
 (let [update (create-update "London"
                             (extract-reading-data fix/a-darksky-reading-body))]
  (fact "it should return a sequence"
    (seq? update) => true)
  (fact "it should contain 14 items"
    (count update) => 14)
  (fact "it should contain some correct data"
    (not (empty? (filter #(:readings/sunrise %) update))) => true
    (not (empty? (filter #(:readings/icon %) update))) => true)))

(facts "about 'log-one-reading'"
  (let [result (->> (get-darksky-data "51.317,0.057")
                    (extract-reading-data)
                    (create-update "London")
                    (log-one-reading conn))]
    (fact "it should return a map"
      (map? result) => true)
    (fact "results should conain valid data"
      (type (result :db-before)) => datomic.db.Db
      (type (result :db-after)) => datomic.db.Db
      (nil? (result :tx-data)) => false)))
