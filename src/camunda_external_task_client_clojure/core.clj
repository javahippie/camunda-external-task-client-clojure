(ns camunda-external-task-client-clojure.core
  (:require [camunda-external-task-client-clojure.external-task :as external-task]))

(let [url "http://localhost:8080/engine-rest"
      lock-duration 1000]
  (external-task/subscribe {:url url
                            :topic "provide_input"
                            :lock-duration lock-duration
                            :handler (fn [et] {"total_amount" 1000
                                               "total_headcount" 4})})

  (external-task/subscribe {:url url
                            :topic "calculate_result"
                            :lock-duration lock-duration
                            :handler (fn [et] (let [total-amount (.getVariable et "total_amount")
                                                    total-headcount (.getVariable et "total_headcount")]
                                                {"amount_per_person" (/ total-amount total-headcount)}))}))
