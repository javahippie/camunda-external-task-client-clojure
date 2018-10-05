(ns camunda-external-task-client-clojure.external-task
  (:import [org.camunda.bpm.client ExternalTaskClient])
  (:require [clojure.walk :as walk]))

(defn- java-map-to-clj [map]
  (walk/keywordize-keys (into {} map)))

(defn- clj-map-to-java [map]
  (java.util.HashMap. (walk/stringify-keys map)))

(defn- set-up [url]
  (.build (.baseUrl (ExternalTaskClient/create) url)))

(defn- handle! [handler-func]
  (reify org.camunda.bpm.client.task.ExternalTaskHandler
    (execute [this external-task external-task-service]
      (.complete external-task-service
                 external-task
                 (clj-map-to-java
                  (handler-func external-task
                                (java-map-to-clj (.getAllVariables external-task))))))))

(defn subscribe [{url :url
                  topic :topic
                  lock-duration :lock-duration
                  handler :handler}]
  (let [client (set-up url)]
    (.open (.handler (.lockDuration (.subscribe client topic)
                                    lock-duration)
                     (handle! handler)))))

