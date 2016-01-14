package com.grimaldos.keradtweet.di.module;

import com.grimaldos.keradtweet.TweetRepository;
import com.grimaldos.keradtweet.TweetRepositoryImpl;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module public class RepositoryModule {
  @Provides @Singleton TweetRepository provideTweetRepository(TweetRepositoryImpl repository) {
    return repository;
  }
}
