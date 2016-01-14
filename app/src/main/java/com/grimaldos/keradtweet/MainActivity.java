package com.grimaldos.keradtweet;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.grimaldos.keradtweet.di.component.DaggerMainActivityComponent;
import com.grimaldos.keradtweet.di.component.MainActivityComponent;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetView;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.inject.Inject;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

  @Bind(R.id.main_view) RelativeLayout mainView;
  @Bind(R.id.main_container) FrameLayout mainContainer;
  @Bind(R.id.shadow_layer) FrameLayout shadowLayer;
  @Bind(R.id.tweet_progress) CircularProgressBar tweetProgressBar;

  @Inject TweetRepository tweetRepository;

  private MainActivityComponent component;
  private boolean shortFormat = true;
  private boolean connecting = false;
  List<Tweet> tweetList = new ArrayList<>();
  Timer tweetDisplay = new Timer();
  int position = 0;

  public MainActivityComponent component() {
    if (component == null) {
      component = DaggerMainActivityComponent.builder()
          .applicationComponent(((KeradTweetApp) getApplication()).component())
          .build();
    }
    return component;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    component().inject(this);
    ButterKnife.bind(this);
    logInTwitter();
  }

  @Override protected void onPause() {
    super.onPause();
    tweetDisplay.cancel();
  }

  @Override protected void onResume() {
    super.onResume();
    if (tweetList.size() > 0) showTweets();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_refresh:
        if (!connecting) {
          obtainTweets();
        }
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  private void logInTwitter() {
    TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
      @Override public void success(Result<AppSession> result) {
        if (!connecting) obtainTweets();
      }

      @Override public void failure(TwitterException e) {
        shadowLayer.setVisibility(View.GONE);
        tweetProgressBar.setVisibility(View.GONE);
        Snackbar.make(mainView, "There was an error with the connection.", Snackbar.LENGTH_LONG)
            .show();
        e.printStackTrace();
      }
    });
  }

  private void obtainTweets() {
    shadowLayer.setVisibility(View.VISIBLE);
    tweetProgressBar.setVisibility(View.VISIBLE);
    connecting = true;
    tweetDisplay.cancel();
    position = 0;
    tweetRepository.obtainTweets(setTweetQuery(), new TweetSubscriber());
  }

  private String setTweetQuery() {
    String query = shortFormat ? "\"it's $$$ and\"-RT" : "\"it is $$$ and\"-RT";
    Calendar cal = Calendar.getInstance();
    int minutes = cal.get(Calendar.MINUTE);

    if (DateFormat.is24HourFormat(this)) {
      int hours = cal.get(Calendar.HOUR_OF_DAY);
      query = query.replace("$$$", (hours < 10 ? "0" + hours : hours) +
          ":" + (minutes < 10 ? "0" + minutes : minutes));
    } else {
      int hours = cal.get(Calendar.HOUR);
      query = query.replace("$$$", hours
          + ":"
          + (minutes < 10 ? "0" + minutes : minutes)
          + " "
          + new DateFormatSymbols().getAmPmStrings()[cal.get(Calendar.AM_PM)]);
    }

    String uriQuery = "";

    try {
      uriQuery = URLEncoder.encode(query, "utf-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    return uriQuery;
  }

  private void showTweets() {
    tweetDisplay = new Timer();
    tweetDisplay.scheduleAtFixedRate(new TimerTask() {
      @Override public void run() {
        runOnUiThread(new Runnable() {
          @Override public void run() {
            if (position >= tweetList.size()) position = 0;
            mainContainer.removeAllViews();
            mainContainer.addView(new TweetView(MainActivity.this, tweetList.get(position)));
            position++;
          }
        });
      }
    }, 0, 10000);
  }

  private final class TweetSubscriber extends Subscriber<Search> {

    @Override public void onCompleted() {
      shadowLayer.setVisibility(View.GONE);
      tweetProgressBar.setVisibility(View.GONE);
      connecting = false;
    }

    @Override public void onError(Throwable e) {
      shadowLayer.setVisibility(View.GONE);
      tweetProgressBar.setVisibility(View.GONE);
      connecting = false;
      Snackbar.make(mainView, "There was an error with the connection.", Snackbar.LENGTH_LONG)
          .show();
      e.printStackTrace();
    }

    @Override public void onNext(Search search) {
      if (search.tweets.size() > 0) {
        tweetList.clear();
        tweetList.addAll(search.tweets);

        Collections.sort(tweetList, TweetSort.RETWEET_COUNT.getTweetComparator());

        showTweets();
      } else if (shortFormat) {
        // Short format found no results, trying long format
        shortFormat = !shortFormat;
        shadowLayer.setVisibility(View.VISIBLE);
        tweetProgressBar.setVisibility(View.VISIBLE);
        obtainTweets();
      } else {
        Snackbar.make(mainView, "No Tweets were found, please try again in a minute.",
            Snackbar.LENGTH_LONG).show();
      }
    }
  }
}
