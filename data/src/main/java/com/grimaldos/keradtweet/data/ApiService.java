package com.grimaldos.keradtweet.data;

import com.twitter.sdk.android.core.models.Search;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Query;
import rx.Observable;

public interface ApiService {
  @GET("/search/tweets.json") Observable<Search> getTweets(@Header("Authorization") String bearer,
      @Query("q") String tag);
}
