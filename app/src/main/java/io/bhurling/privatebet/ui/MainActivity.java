package io.bhurling.privatebet.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import io.bhurling.privatebet.Application;
import io.bhurling.privatebet.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_AUTH = 0;

    @Inject
    MainPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Application.component(this).inject(this);

        if (savedInstanceState == null) {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setProviders(
                                        AuthUI.GOOGLE_PROVIDER
                                )
                                .build(),
                        REQUEST_CODE_AUTH
                );
            }
        }
    }
}
