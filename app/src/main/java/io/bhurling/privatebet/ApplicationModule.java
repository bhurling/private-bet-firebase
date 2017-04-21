package io.bhurling.privatebet;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.bhurling.privatebet.dependencies.BetsReference;
import io.bhurling.privatebet.dependencies.FeedReference;

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    public Context provideAppContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    public FirebaseDatabase provideFirebaseDatabase() {
        return FirebaseDatabase.getInstance();
    }

    @Provides
    @Singleton
    public FirebaseAuth provideFirebaseAuthentication() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    public FirebaseUser provideFirebaseUser(FirebaseAuth auth) {
        return auth.getCurrentUser();
    }

    @FeedReference
    @Provides
    public DatabaseReference provideFeed(FirebaseDatabase database, FirebaseUser user) {
        return database.getReference("feeds").child(user.getUid());
    }

    @BetsReference
    @Provides
    public DatabaseReference provideBets(FirebaseDatabase database) {
        return database.getReference("bets");
    }

}
