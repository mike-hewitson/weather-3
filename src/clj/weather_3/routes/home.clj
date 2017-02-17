(ns weather-3.routes.home
  (:require [weather-3.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [weather-3.db.core :as db]
            [clj-time.core :as t]
            [clj-time.coerce :as c]
            [clj-time.periodic :as p]
            [clojure.math.numeric-tower :as m]))

(def wind-directions
  ["N" "NE" "E" "SE" "S" "SW" "W" "NW"])

(defn get-direction
  "translater wind bearing to direction in text"
  [bearing]
  (wind-directions (mod (m/round (/ bearing 45)) 8)))

(defn add-direction-into-readings
  "include the direction element into the reading"
  [readings]
  (map (fn [reading]
         (assoc reading :wind-direction (get-direction (:readings/wind-bearing reading))))
       readings))

(defn home-page []
  (let [readings (db/get-reading-at-time)]
   (layout/render
    "home.html"
    {:readings (add-direction-into-readings (:readings readings))
     :created-at (:as-at readings)})))

(defroutes home-routes
  (GET "/" [] (home-page)))
  ; (GET "/summary" [] (summary-page)))

;TODO create seperate routes for history and summary
;TODO remove requires no needed
