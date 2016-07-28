package io.bhurling.privatebet;

import javax.inject.Singleton;

import dagger.Component;
import io.bhurling.privatebet.ui.MainActivity;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(MainActivity activity);
}
