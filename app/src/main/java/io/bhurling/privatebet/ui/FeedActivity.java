package io.bhurling.privatebet.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import io.bhurling.privatebet.Application;
import io.bhurling.privatebet.presenter.FeedPresenter;

public class FeedActivity extends AppCompatActivity implements FeedPresenter.View {

    @Inject
    FeedPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Application.component(this).inject(this);

        presenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.detachView();
    }
}
