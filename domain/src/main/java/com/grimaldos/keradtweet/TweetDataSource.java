package com.grimaldos.keradtweet;

import rx.Subscriber;

public interface TweetDataSource {
  void obtainTweets(String query, Subscriber subscriber);
}
