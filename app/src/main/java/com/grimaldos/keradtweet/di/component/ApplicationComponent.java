package com.grimaldos.keradtweet.di.component;

import android.app.Application;
import android.content.SharedPreferences;
import com.grimaldos.keradtweet.KeradTweetApp;
import com.grimaldos.keradtweet.TweetDataSource;
import com.grimaldos.keradtweet.TweetRepository;
import com.grimaldos.keradtweet.data.ApiService;
import com.grimaldos.keradtweet.di.module.ApiModule;
import com.grimaldos.keradtweet.di.module.ApplicationModule;
import com.grimaldos.keradtweet.di.module.DataSourceModule;
import com.grimaldos.keradtweet.di.module.RepositoryModule;
import dagger.Component;
import javax.inject.Singleton;

@Singleton @Component(modules = {
    ApplicationModule.class, RepositoryModule.class, DataSourceModule.class, ApiModule.class
}) public interface ApplicationComponent {
  void inject(KeradTweetApp application);

  Application application();

  SharedPreferences sharedPreferences();

  TweetRepository tweetRepository();

  TweetDataSource tweetDataSource();

  ApiService apiService();
}
