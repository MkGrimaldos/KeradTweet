package com.grimaldos.keradtweet.di.component;

import com.grimaldos.keradtweet.MainActivity;
import com.grimaldos.keradtweet.di.annotations.PerActivity;
import dagger.Component;

@PerActivity @Component(dependencies = ApplicationComponent.class)
public interface MainActivityComponent {
  void inject(MainActivity mainActivity);
}
