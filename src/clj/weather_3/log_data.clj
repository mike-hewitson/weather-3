(ns weather-3.log-data
  (:require [datomic.api :as d]
            [clj-http.client :as client]
            [clojure.tools.logging :as log]
            [cprop.core :refer [load-config]]
            [cprop.source :as source]))

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
    ["wind-speed" (fn [x] (* x 3.6))]
    ["wind-bearing" long]
    ["pressure" float]
    ["humidity" float]
    ["precip-probability" float]
    ["precip-intensity" float]
    ["cloud-cover" float]]
   [[:daily :summary]
    [:data :sunriseTime]
    [:data :sunsetTime]
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

(defn get-darksky-data
  "retrieve a set of readings from darksky.io for a gps location"
  [gps]
  (let [my-url (str
                "https://api.darksky.net/forecast/62888a9ff1907377b60a866701cf3338/"
                gps
                "?units=si&exclude=minutely,hourly,alerts,flags")]
    (:body (client/get my-url {:as :json}))))


(defn extract-reading-data
  "extract the required data elements from the darksky message body"
  [body]
  (zipmap (first reading-names)
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
  "create a Datomic update map for a location reading map"
  [location reading-map]
  (map (fn [[[name & [cast]] value]]
         {:db/id [:location/name location] (keyword (str "readings/" name))
          (if cast (cast value) value)})
       reading-map))

(def conn
  "load connection from config"
  (let [uri (:database-url
              (load-config :merge
                [(source/from-system-props)
                 (source/from-env)]))]
    (d/connect uri)))

(defn log-one-reading
  "logs readings for one location"
  [conn reading]
  @(d/transact conn reading))

(defn log-readings
  "gets all of the data and writes to database"
  []
  (log/debug
   "the results :"
   (map (fn [[location gps]]
            (->> (get-darksky-data gps)
                 (extract-reading-data)
                 (create-update location)
                 (log-one-reading conn)))
        locations)))

(defn -main [& args]
  (log-readings)
  (log/info "Logged one set of readings")
  (System/exit 0))
