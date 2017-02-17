(ns weather-3.routes.history
  (:require [weather-3.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [weather-3.db.core :as db]
            [clj-time.core :as t]
            [clj-time.coerce :as c]
            [clj-time.periodic :as p]))

(defn create-history-seq
  "create a sequence of 50 dates between a date and today"
  [days-back]
  (let [interval (int (/ (* days-back 24  3600) 49))
        from-date (t/minus (t/now) (t/days days-back))
        dates-at (take 50 (p/periodic-seq from-date (t/seconds interval)))]
   (map #(db/get-reading-at-time (c/to-date %)) dates-at)))

(defn create-display-list
  [readings-list]
  (map (fn [x]
         (let [reading (first (:readings x))]
           {:as-at (:as-at x)
            :location/name (:location/name reading)}))
       readings-list))

(defn history-page []
   (layout/render
    "history.html"
    {:readings (create-display-list (create-history-seq 2))}))

(defroutes history-routes
  (GET "/history" [] (history-page)))

;TODO adjust readings resolution when graphs are visual
;TODO add a few other data items here
