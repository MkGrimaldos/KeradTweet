package com.grimaldos.keradtweet;

import rx.Subscriber;

public interface TweetRepository {
  void obtainTweets(String query, Subscriber subscriber);
}
