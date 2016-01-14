package com.grimaldos.keradtweet;

import javax.inject.Inject;
import rx.Subscriber;

public class TweetRepositoryImpl implements TweetRepository {

  private TweetDataSource tweetDataSource;

  @Inject public TweetRepositoryImpl(TweetDataSource tweetDataSource) {
    this.tweetDataSource = tweetDataSource;
  }

  @Override public void obtainTweets(String query, Subscriber subscriber) {
    tweetDataSource.obtainTweets(query, subscriber);
  }
}
