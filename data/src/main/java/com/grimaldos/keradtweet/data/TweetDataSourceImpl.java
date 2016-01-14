package com.grimaldos.keradtweet.data;

import com.grimaldos.keradtweet.TweetDataSource;
import com.twitter.sdk.android.core.TwitterCore;
import javax.inject.Inject;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TweetDataSourceImpl implements TweetDataSource {

  ApiService apiService;
  // Parser parser;

  @Inject public TweetDataSourceImpl(ApiService apiService) {
    this.apiService = apiService;
  }

  @Override public void obtainTweets(final String query, Subscriber subscriber) {

    String authorization = "Bearer " + TwitterCore.getInstance()
        .getAppSessionManager()
        .getActiveSession()
        .getAuthToken()
        .getAccessToken();

    apiService.getTweets(authorization, query)
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);

    //TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
    //twitterApiClient.getSearchService()
    //    .tweets(query, null, "en", null, null, null, null, null, null, false,
    //        new Callback<Search>() {
    //          @Override public void success(Result<Search> result) {
    //            for (Tweet tweet : result.data.tweets) {
    //              Log.d("MIGUEL", tweet.text);
    //            }
    //          }
    //
    //          @Override public void failure(TwitterException e) {
    //            e.printStackTrace();
    //          }
    //        });
  }
}
