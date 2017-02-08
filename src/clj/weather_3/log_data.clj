(ns weather-3.log-data
  (:require [datomic.api :as d]
            [clj-http.client :as client]
            [clojure.tools.logging :as log]
            [cprop.core :refer [load-config]]
            [cprop.source :as source]))

; TODO refactor code, this is ugly

(def icons-transform
  { "day-sunny" "wi-day-sunny"
    "clear-night" "wi-night-clear"
    "rain" "wi-rain"
    "wind" "wi-strong-wind"
    "snow" "wi-snow"
    "sleet" "wi-sleet"
    "strong-wind" "wi-wind"
    "fog" "wi-fog"
    "cloudy" "wi-cloudy"
    "day-cloudy" "wi-day-cloudy"
    "partly-cloudy-day" "wi-day-sunny-overcast"
    "night-cloudy" "wi-night-cloudy"
    "partly-cloudy-night" "wi-night-partly-cloudy"
    "hail" "wi-hail"
    "thunderstorm" "wi-thunderstorm"
    "tornado" "wi-tornado"
    "clear-day" "wi-day-sunny"});

(def reading-names
  [[["week-summary"]
    ["sunrise"]
    ["sunset"]
    ["day-summary"]
    ["now-summary"]
    ["icon" (fn [x] (icons-transform x))]
    ; ["icon"]
    ["temperature" float]
    ["wind-speed" float]
    ["wind-bearing" long]
    ["pressure" float]
    ["humidity" float]
    ["precip-probability" float]
    ["precip-intensity" float]
    ["cloud-cover" float]]
   [[:daily :summary]
    [:data :sunsetTime]
    [:data :sunriseTime]
    [:data :summary]
    [:currently :summary]
    [:currently :icon]
    [:currently :temperature]
    [:currently :windSpeed]
    [:currently :windBearing]
    [:currently :pressure]
    [:currently :humidity]
    [:currently :precipProbability]
    [:currently :precipIntensity]
    [:currently :cloudCover]]])

(def locations
  [["London" "51.317,0.057"]
   ["Sandton" "-26.097,28.053"]
   ["Paradise Beach" "-34.089,24.903"]])

; TODO split out the data fetch doing two things
; refactor code to make it testable

(defn get-data
  "retrive data from the darksky service for a specific location and format it into the correct tupe and structure"
  [gps]
  (let [my-url (str
                 "https://api.darksky.net/forecast/62888a9ff1907377b60a866701cf3338/"
                 gps
                 "?units=si&exclude=minutely,hourly,alerts,flags")
        darksky-data (client/get my-url {:as :json})
        body (:body darksky-data)]
    (map (fn [[k v]]
           (let [data (v (first (:data (:daily body))))]
            (cond
              (= k :daily) (v (:daily body))
              (= k :data)  (if (string? data)
                             data
                             (java.util.Date. (* 1000 data)))
              (= k :currently) (v (:currently body))
              :else nil)))
         (last reading-names))))

(defn create-update
  "create a datomic transaction collection for one location"
  [location gps]
  (map (fn [[[name & [cast]] value]] {:db/id [:location/name location] (keyword (str "readings/" name))
                                      (if cast (cast value) value)})
       (zipmap (first reading-names) (get-data gps))))

(defn readings-data
  "get all reading data from darksky service"
  []
  (map (fn [[name gps]] (create-update name gps)) locations))

(def conn
  "load connection from config"
  (let [uri (:database-url
              (load-config :merge
                [(source/from-system-props)
                 (source/from-env)]))]
    (d/connect uri)))

(defn log-one-reading
  "logs readings for one location"
  [reading]
  @(d/transact conn reading))

; TODO this feels like it needs to be fixed

(defn log-readings
  "gets all of the data and writes to database"
  []
  (let [data (readings-data)]
    (log/debug "the results :" (map #(log-one-reading %) data))))

(defn -main [& args]
  (log-readings)
  (log/info "Logged one set of readings")
  (System/exit 0))
