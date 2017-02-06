(ns user
  (:require [mount.core :as mount]
            weather-3.core))

(defn start []
  (mount/start-without #'weather-3.core/http-server
                       #'weather-3.core/repl-server))

(defn stop []
  (mount/stop-except #'weather-3.core/http-server
                     #'weather-3.core/repl-server))

(defn restart []
  (stop)
  (start))


