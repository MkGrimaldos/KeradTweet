package com.grimaldos.keradtweet;

import android.app.Application;

import com.grimaldos.keradtweet.di.module.ApiModule;
import com.grimaldos.keradtweet.di.component.ApplicationComponent;
import com.grimaldos.keradtweet.di.module.ApplicationModule;
import com.grimaldos.keradtweet.di.component.DaggerApplicationComponent;
import com.grimaldos.keradtweet.di.module.DataSourceModule;
import com.grimaldos.keradtweet.di.module.RepositoryModule;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

public class KeradTweetApp extends Application {

    private ApplicationComponent applicationComponent;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "5cwiVqujRzyUodOVn79HRyIFW";
    private static final String TWITTER_SECRET = "6j2wYtecb0Qfm72AYD3fBX0iEbl1R3JIAVHq8VEhjAEdtnhwRY";

    @Override
    public void onCreate() {
        super.onCreate();
        setupAppComponent();
        loadTwitterAPI();
    }

    private void setupAppComponent() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .repositoryModule(new RepositoryModule())
                .dataSourceModule(new DataSourceModule())
                .apiModule(new ApiModule())
                .build();
    }

    public ApplicationComponent component() {
        return applicationComponent;
    }

    private void loadTwitterAPI() {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
    }
}
