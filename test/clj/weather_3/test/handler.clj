(ns weather-3.test.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [weather-3.handler :refer :all]
            [midje.sweet]))

; (deftest test-app
;   (testing "main route"
;     (let [response ((app) (request :get "/"))]
;       (is (= 200 (:status response)))))
;
;   (testing "not-found route"
;     (let [response ((app) (request :get "/invalid"))]
;       (is (= 404 (:status response))))))

; TODO fix up error when test pages
