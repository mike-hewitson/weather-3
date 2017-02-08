(ns weather-3.db.core
  (:require [datomic.api :as d]
            [mount.core :refer [defstate]]
            [clojure.tools.logging :as log]
            [weather-3.config :refer [env]]))

(defstate conn
          :start (-> env :database-url d/connect)
          :stop (-> conn .release))

; TODO remove commented out code

; (defn create-schema []
;   (let [schema [{:db/ident              :user/id
;                  :db/valueType          :db.type/string
;                  :db/cardinality        :db.cardinality/one
;                  :db.install/_attribute :db.part/db}
;                 {:db/ident              :user/first-name
;                  :db/valueType          :db.type/string
;                  :db/cardinality        :db.cardinality/one
;                  :db.install/_attribute :db.part/db}
;                 {:db/ident              :user/last-name
;                  :db/valueType          :db.type/string
;                  :db/cardinality        :db.cardinality/one
;                  :db.install/_attribute :db.part/db}
;                 {:db/ident              :user/email
;                  :db/valueType          :db.type/string
;                  :db/cardinality        :db.cardinality/one
;                  :db.install/_attribute :db.part/db}]]
;     @(d/transact conn schema)))

(defn entity [conn id]
  (d/entity (d/db conn) id))

(defn touch [conn results]
  "takes 'entity ids' results from a query
    e.g. '#{[272678883689461] [272678883689462] [272678883689459] [272678883689457]}'"
  (let [e (partial entity conn)]
    (map #(-> % first e d/touch) results)))

; (defn add-user [conn {:keys [id first-name last-name email]}]
;   @(d/transact conn [{:db/id           id
;                       :user/first-name first-name
;                       :user/last-name  last-name
;                       :user/email      email}]))

(defn find-user [conn id]
  (let [user (d/q '[:find ?e :in $ ?id
                      :where [?e :user/id ?id]]
                   (d/db conn)
                   id)]
    (touch conn user)))

; TODO include the time capability
; TODO return the created at date in the message

(defn get-reading-at-time
  "returns a collection a set of reading data for all locations at a specific time (optional)"
  [& time]
  (map (fn [location] (d/pull (d/db conn)
                              '[*]
                              [:location/name location]))
       ["Sandton" "Paradise Beach" "London"]))

; (def bob (d/connect (:database-url env)))
;
; (d/pull (d/db bob) '[*] [:location/name "London"])
; (d/pull (d/db bob) '[*] 17592186045419)
