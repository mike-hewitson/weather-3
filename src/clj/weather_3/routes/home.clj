(ns weather-3.routes.home
  (:require [weather-3.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [weather-3.db.core :as db]))

; (defn home-page []
;   (layout/render
;     "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn home-page []
  (let [readings (db/get-reading-at-time)]
   (layout/render
     "home.html"
    (merge {:readings readings}
           {:created-at (new java.util.Date)}))))
    ;  {:created-at "wtf"})))
      ;  "home.html"
                  ; {:readings (seq (reading :sensors))}
                  ; {:created-at (reading :createdAt)}})))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page)))

; TODO change to return date in message from function
