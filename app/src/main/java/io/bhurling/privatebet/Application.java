package io.bhurling.privatebet;

import android.content.Context;

public class Application extends android.app.Application {

    private ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (mComponent == null) {
            mComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
    }

    public static ApplicationComponent component(Context context) {
        return ((Application) context.getApplicationContext()).mComponent;
    }
}
