package io.bhurling.privatebet.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import io.bhurling.privatebet.Application;
import io.bhurling.privatebet.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity {

    @Inject
    MainPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Application.component(this).inject(this);
    }
}
