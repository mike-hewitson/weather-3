(ns weather-3.routes.home
  (:require [weather-3.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]))

(defn home-page []
  (layout/render
    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

; (defn home-page []
;   (let [reading (first (db/get-latest))]
;    (layout/render
;      "home.html" (merge
;                   {:readings (seq (reading :sensors))}
;                   {:created-at (reading :createdAt)}))))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page)))
