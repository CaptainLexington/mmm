(ns mmm.files
  (:require [aws.sdk.s3 :as s3]))

(def cred {:access-key "uh uh uh uh uh"
           :secret-key "you didn't say the magic word"})


(defn add-poster [file-name poster]
  (do
    (s3/put-object cred "mdntmvsmpls" file-name poster)
    (s3/update-object-acl cred "mdntmvsmpls" file-name (s3/grant :all-users :read))))