(ns weather-3.test.db.core
  (:require [ring.mock.request :refer :all]
            [weather-3.db.core :refer :all]
            [weather-3.db-init :as init]
            [weather-3.test.fixtures :as fix]
            [midje.sweet :refer :all]))


; TODO complete test
; Need to add fixtures with specfic 


; this is currently hard-coded to prevent nasty things hapenning
(def uri "datomic:free://localhost:4334/weather_3_test")

(defn initialize-db
  "initialise a database reading for testing"
  [uri]
  (do
   (init/delete-database uri)
   (init/create-database uri)
   (init/add-schema uri)
   (init/add-locations uri)))

(facts "about 'get-reading-at-a-time'"
    ; (initialize-db uri)
  (let [data (first (:readings (get-reading-at-time)))]
    (fact "given nothing it should return a map"
      (map? data) => true)
    (fact "it should contain some correct data"
      (:readings/cloud-cover data)) => truthy
      (:readings/now-summary data)) => truthy)
