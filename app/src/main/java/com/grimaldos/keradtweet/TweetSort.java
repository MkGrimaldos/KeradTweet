package com.grimaldos.keradtweet;

import com.twitter.sdk.android.core.models.Tweet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public enum TweetSort {

  RETWEET_COUNT;

  private final Comparator<Tweet> tweetComparator = new Comparator<Tweet>() {
    @Override public int compare(Tweet tweet1, Tweet tweet2) {
      if (tweet1.retweetCount != tweet2.retweetCount) {
        return (tweet1.retweetCount > tweet2.retweetCount ? -1 : 1);
      } else {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd kk:mm:ss ZZZ yyyy");
        try {
          Date date1 = dateFormat.parse(tweet1.createdAt);
          Date date2 = dateFormat.parse(tweet2.createdAt);

          return (date1.compareTo(date2) > 0 ? -1 : (date1.compareTo(date2) == 0 ? 0 : 1));
        } catch (ParseException e) {
          e.printStackTrace();
        }
        return 0;
      }
    }
  };

  public Comparator<Tweet> getTweetComparator() {
    switch (this) {
      case RETWEET_COUNT:
      default:
        return tweetComparator;
    }
  }
}