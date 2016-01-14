package com.grimaldos.keradtweet.di.module;

import com.grimaldos.keradtweet.TweetDataSource;
import com.grimaldos.keradtweet.data.TweetDataSourceImpl;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module public class DataSourceModule {
  @Provides @Singleton TweetDataSource provideTweetDataSource(TweetDataSourceImpl dataSource) {
    return dataSource;
  }
}
