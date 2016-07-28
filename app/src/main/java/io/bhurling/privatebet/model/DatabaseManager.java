package io.bhurling.privatebet.model;

import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DatabaseManager {

    private final FirebaseDatabase mDatabase;

    @Inject
    public DatabaseManager(FirebaseDatabase database) {
        mDatabase = database;
    }
}
