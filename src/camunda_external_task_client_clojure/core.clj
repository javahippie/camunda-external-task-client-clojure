(ns camunda-external-task-client-clojure.core
  (:require [camunda-external-task-client-clojure.external-task :as et]))

(let [url "http://localhost:8080/engine-rest"
      lock-duration 20]
  
  (et/subscribe {:url url
                 :topic "provide_input"
                 :lock-duration lock-duration
                 :handler (fn [task vars]
                            {:total_amount 1000
                             :total_headcount 4})})

  (et/subscribe {:url url
                 :topic "calculate_result"
                 :lock-duration lock-duration
                 :handler (fn [task vars]
                            {:amount_per_headcount (/ (:total_amount vars)
                                                      (:total_headcount vars))})}))
