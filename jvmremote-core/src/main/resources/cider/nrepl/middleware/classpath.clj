(ns cider.nrepl.middleware.classpath
  (:require
   [cider.nrepl.middleware.util.error-handling :refer [with-safe-transport]]
   [clojure.java.io :as io]
   [cider.nrepl.inlined-deps.orchard.v0v5v6.orchard.java.classpath :as cp]
   [cider.nrepl.inlined-deps.orchard.v0v5v6.orchard.misc :as misc]))

(defn file-url?
  [u]
  (and (misc/url? u)
       (= (.getProtocol u) "file")))

(defn classpath-reply [msg]
  {:classpath (->> (cp/classpath)
                   (filter file-url?)
                   (map io/as-file)
                   (map str))})

(defn handle-classpath [handler msg]
  (with-safe-transport handler msg
    "classpath" classpath-reply))
