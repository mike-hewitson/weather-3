(ns weather-3.routes.home
  (:require [weather-3.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [weather-3.db.core :as db]
            [clj-time.core :as t]
            [clj-time.coerce :as c]))

(defn home-page []
  (let [readings (db/get-reading-at-time)]
   (layout/render
    "home.html"
    (merge {:readings (:readings readings)}
           {:created-at (:as-at readings)}))))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page)))

(defn create-history-seq
  "create a sequence of 50 dates between a date and today"
  [days-back])


(def range (* 10 24 3600))
(def interval (/ range 50))
(take 10 (range (- (c/to-long (t/now)) range)
                (c/to-long (t/now))
                interval))
(take 20 (range 0 100 9))
(type (c/to-long (t/now)))
