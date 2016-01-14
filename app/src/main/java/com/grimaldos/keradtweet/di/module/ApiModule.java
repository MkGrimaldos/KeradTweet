package com.grimaldos.keradtweet.di.module;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.grimaldos.keradtweet.BuildConfig;
import com.grimaldos.keradtweet.data.ApiService;
import dagger.Module;
import dagger.Provides;
import java.util.Date;
import javax.inject.Singleton;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

@Module public class ApiModule {
  private final String ENDPOINT = "https://api.twitter.com/1.1";

  @Provides @Singleton ApiService provideApiService() {
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateTypeAdapter()).create();
    return new RestAdapter.Builder().setLogLevel(
        BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
        .setLog(new RestAdapter.Log() {
          @Override public void log(String message) {
            Log.d("Retrofit", message);
          }
        })
        .setConverter(new GsonConverter(gson))
        .setEndpoint(ENDPOINT)
        .build()
        .create(ApiService.class);
  }

  //    @Provides @Singleton ApiService provideApiService(Endpoint endpoint, Converter converter) {
  //        return new RestAdapter.Builder()
  //                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
  //                .setLog(new RestAdapter.Log() {
  //                    @Override
  //                    public void log(String message) {
  //                        Log.d("Retrofit", message);
  //                    }
  //                })
  //                .setConverter(converter)
  //                .setEndpoint(endpoint)
  //                .build()
  //                .create(ApiService.class);
  //    }
}
