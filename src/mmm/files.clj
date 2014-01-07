(ns mmm.files
  (:require [aws.sdk.s3 :as s3]))

(def cred {:access-key "AKIAICILDFDJKJO6TZAA"
           :secret-key "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"})


(defn add-poster [file-name poster]
  (do
    (s3/put-object cred "mdntmvsmpls" file-name poster)
    (s3/update-object-acl cred "mdntmvsmpls" file-name (s3/grant :all-users :read))))