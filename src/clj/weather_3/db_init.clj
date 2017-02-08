;   Copyright (c) Cognitect, Inc. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.
(ns weather-3.db-init
  (:require [datomic.api :as d]
            [clojure.tools.logging :as log]))


(def uri "datomic:free://localhost:4334/weather_3_dev")
; (def uri "datomic:free://localhost:4334/weather_3_test")
; (def uri "datomic:free://localhost:4334/weather_3")

(defn create-database []
  (d/create-database uri))


(def schema
  [{:db/id #db/id [:db.part/db]
    :db/ident :location/name
    :db/valueType :db.type/string
    :db/unique :db.unique/identity
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}
   {:db/id #db/id [:db.part/db]
    :db/ident :readings/now-summary
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}
   {:db/id #db/id [:db.part/db]
    :db/ident :readings/day-summary
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}
   {:db/id #db/id [:db.part/db]
    :db/ident :readings/week-summary
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}
   {:db/id #db/id [:db.part/db]
    :db/ident :readings/sunrise
    :db/valueType :db.type/instant
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}
   {:db/id #db/id [:db.part/db]
    :db/ident :readings/sunset
    :db/valueType :db.type/instant
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}
   {:db/id #db/id [:db.part/db]
    :db/ident :readings/icon
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}
   {:db/id #db/id [:db.part/db]
    :db/ident :readings/temperature
    :db/valueType :db.type/float
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}
   {:db/id #db/id [:db.part/db]
    :db/ident :readings/wind-speed
    :db/valueType :db.type/float
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}
   {:db/id #db/id [:db.part/db]
    :db/ident :readings/wind-bearing
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}
   {:db/id #db/id [:db.part/db]
    :db/ident :readings/pressure
    :db/valueType :db.type/float
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}
   {:db/id #db/id [:db.part/db]
    :db/ident :readings/humidity
    :db/valueType :db.type/float
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}
   {:db/id #db/id [:db.part/db]
    :db/ident :readings/precip-probability
    :db/valueType :db.type/float
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}
   {:db/id #db/id [:db.part/db]
    :db/ident :readings/precip-intensity
    :db/valueType :db.type/float
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}
   {:db/id #db/id [:db.part/db]
    :db/ident :readings/cloud-cover
    :db/valueType :db.type/float
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}])

  ;; add schema

(defn add-schema []
 (let [conn (d/connect uri)]
   @(d/transact conn schema)))

;; add locations - not required
(defn add-locations []
 (let [conn (d/connect uri)]
   @(d/transact
     conn
     [[:db/add (d/tempid :db.part/user) :location/name "Sandton"]
      [:db/add (d/tempid :db.part/user) :location/name "London"]
      [:db/add (d/tempid :db.part/user) :location/name "Paradise Beach"]])))
