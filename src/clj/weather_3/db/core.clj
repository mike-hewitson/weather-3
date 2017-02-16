(ns weather-3.db.core
  (:require [datomic.api :as d]
            [mount.core :refer [defstate]]
            [clojure.tools.logging :as log]
            [weather-3.config :refer [env]]
            [clojure.pprint :as pp]))

(defstate conn
          :start (-> env :database-url d/connect)
          :stop (-> conn .release))

(defonce locations
  ["Sandton" "Paradise Beach" "London"])

(defn get-reading-at-time
  "returns a collection a set of reading data for all locations at a specific time (optional)"
  [& [time]]
  (let [db (cond-> (d/db conn)
                   time (d/as-of time))]
   {:readings (map (fn [location]
                      (d/pull db
                              '[*]
                              [:location/name location]))
                   locations)
    :as-at (->> (d/q '[:find (max ?tx) . :in $ [?e ...] :where [?e _ _ ?tx]]
                     db
                     (map (fn [l] [:location/name l]) locations))
                (d/pull db '[*])
                :db/txInstant)}))

(defn print-history
  "print database history for one location"
  [location]
  (->> (d/datoms (d/history (d/db conn))
                 :eavt [:location/name location])
       seq (sort-by :tx)
       (map (fn [[e a v t op]] {:e e :a a :v v :t t :op op}))
       (pp/print-table [:e :a :v :tx :op])))

; TODO create tests for get-reading
