(ns weather-3.db.core
  (:require [datomic.api :as d]
            [mount.core :refer [defstate]]
            [clojure.tools.logging :as log]
            [weather-3.config :refer [env]]
            [clojure.pprint :as pp]))

(defstate conn
          :start (-> env :database-url d/connect)
          :stop (-> conn .release))


; (defn find-user [conn id]
;   (let [user (d/q '[:find ?e :in $ ?id
;                       :where [?e :user/id ?id]]
;                    (d/db conn)
;                    id)]
;     (touch conn user)))

; TODO include the time capability

(defn get-reading-at-time
  "returns a collection a set of reading data for all locations at a specific time (optional)"
  [& time]
  (log/debug "connection")
  (let [db (d/db conn)]
   (merge
    {:readings (map (fn [location]
                      (d/pull db
                              '[*]
                              [:location/name location]))
                    ["Sandton" "Paradise Beach" "London"])}
    {:as-at (->>
             db
             d/basis-t
             d/t->tx
             (d/entity db)
             :db/txInstant)})))

(defn print-history
  "print database history for one location"
  []
  (let [history (d/history (d/db conn))]
   (->> (d/q '[:find ?e ?a ?v ?tx ?op
               :in $
               :where [?e :location/name "London"]
               [?e ?a ?v ?tx ?op]]
             history)
        (map #(zipmap [:e :a :v :tx :op] %))
        (sort-by :tx)
        (pp/print-table [:e :a :v :tx :op]))))

; TODO craete tests for get-reading
