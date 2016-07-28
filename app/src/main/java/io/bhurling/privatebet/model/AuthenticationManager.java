package io.bhurling.privatebet.model;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AuthenticationManager {

    private final FirebaseAuth mAuth;

    @Inject
    public AuthenticationManager(FirebaseAuth auth) {
        mAuth = auth;
    }
}
