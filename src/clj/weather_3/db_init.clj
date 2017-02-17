(ns weather-3.db-init
  (:require [datomic.api :as d]
            [clojure.tools.logging :as log]))


(def uri "datomic:free://localhost:4334/weather_3_dev")
; (def uri "datomic:free://localhost:4334/weather_3_test")
; (def uri "datomic:free://localhost:4334/weather_3")

(defn create-database
  "create database"
  [uri]
  (d/create-database uri))

(defn delete-database
  "delete database"
  [uri]
  (d/delete-database uri))

(def schema
  [{
    :db/ident :location/name
    :db/valueType :db.type/string
    :db/unique :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {
    :db/ident :readings/now-summary
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {
    :db/ident :readings/day-summary
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {
    :db/ident :readings/temperature-max
    :db/valueType :db.type/float
    :db/cardinality :db.cardinality/one}
   {
    :db/ident :readings/week-summary
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {
    :db/ident :readings/sunrise
    :db/valueType :db.type/instant
    :db/cardinality :db.cardinality/one}
   {
    :db/ident :readings/sunset
    :db/valueType :db.type/instant
    :db/cardinality :db.cardinality/one}
   {
    :db/ident :readings/icon
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {
    :db/ident :readings/temperature
    :db/valueType :db.type/float
    :db/cardinality :db.cardinality/one}
   {
    :db/ident :readings/wind-speed
    :db/valueType :db.type/float
    :db/cardinality :db.cardinality/one}
   {
    :db/ident :readings/wind-bearing
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one}
   {
    :db/ident :readings/pressure
    :db/valueType :db.type/float
    :db/cardinality :db.cardinality/one}
   {
    :db/ident :readings/humidity
    :db/valueType :db.type/float
    :db/cardinality :db.cardinality/one}
   {
    :db/ident :readings/precip-probability
    :db/valueType :db.type/float
    :db/cardinality :db.cardinality/one}
   {
    :db/ident :readings/precip-intensity
    :db/valueType :db.type/float
    :db/cardinality :db.cardinality/one}
   {
    :db/ident :readings/cloud-cover
    :db/valueType :db.type/float
    :db/cardinality :db.cardinality/one}])

(defn add-schema
  "add schema to database"
  [uri]
  (let [conn (d/connect uri)]
    @(d/transact conn schema)))

;; add locations - not required
(defn add-locations
  "add locations to database"
  [uri]
  (let [conn (d/connect uri)]
    @(d/transact
      conn
      [[:db/add :location/name "Sandton"]
       [:db/add :location/name "London"]
       [:db/add :location/name "Paradise Beach"]])))
