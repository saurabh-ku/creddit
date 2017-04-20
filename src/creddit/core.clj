(ns creddit.core
  (:require [creddit.client :as client]))

(defprotocol RedditApi
  (frontpage [this] [this limit])
  (subreddit [this subreddit] [this subreddit limit])
  (subreddits [this] [this limit]))

(defrecord CredditClient [credentials]
  RedditApi
  (frontpage [this] (client/frontpage credentials))
  (frontpage [this limit] (client/frontpage credentials limit))
  (subreddit [this subreddit] (client/subreddit credentials subreddit))
  (subreddit [this subreddit limit] (client/subreddit credentials subreddit limit))
  (subreddits [this] (client/subreddits credentials))
  (subreddits [this limit] (client/subreddits credentials limit)))

(defn init
  [credentials]
  (let [response (client/get-access-token credentials)]
    (-> credentials
        (assoc :access-token (:access_token response))
        (assoc :expires-in (+ (System/currentTimeMillis) (:expires_in response)))
        (CredditClient.))))