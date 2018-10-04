(ns camunda-external-task-client-clojure.core
  (:import [org.camunda.bpm.client ExternalTaskClient]))

(defn set-up [url]
  (.build (.baseUrl (ExternalTaskClient/create) url)))

(defn handle! [handler-func]
  (reify org.camunda.bpm.client.task.ExternalTaskHandler
    (execute [this external-task external-task-service]
      (.complete external-task-service external-task (handler-func)))))

(defn subscribe [{url :url
                  topic :topic
                  lock-duration :lock-duration
                  handler :handler}]
  (let [client (set-up url)]
    (.open (.handler (.lockDuration (.subscribe client
                                                topic)
                                    lock-duration)
                     (handle! handler)))))

(subscribe {:url "http://localhost:8080/engine-rest"
             :topic "provide_input"
             :lock-duration 1000
             :handler #({"val" 1})})

