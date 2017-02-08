(ns weather-3.db.core
  (:require [datomic.api :as d]
            [mount.core :refer [defstate]]
            [clojure.tools.logging :as log]
            [weather-3.config :refer [env]]))

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
; TODO fix the last transaction date

(defn get-reading-at-time
  "returns a collection a set of reading data for all locations at a specific time (optional)"
  [& time]
  (let [db (d/db conn)]
   (merge
    {:readings (map (fn [location]
                      (d/pull db
                              '[*]
                              [:location/name location]))
                    ["Sandton" "Paradise Beach" "London"])}
    {:as-at (java.util.Date. (d/t->tx (d/basis-t db)))})))
