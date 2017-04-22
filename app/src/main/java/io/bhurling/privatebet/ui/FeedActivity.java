package io.bhurling.privatebet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.bhurling.privatebet.Application;
import io.bhurling.privatebet.R;
import io.bhurling.privatebet.presenter.FeedPresenter;

public class FeedActivity extends AppCompatActivity implements FeedPresenter.View {

    @Inject
    FeedPresenter presenter;

    @Inject
    FeedAdapter adapter;

    @BindView(R.id.feed)
    RecyclerView feed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        Application.component(this).inject(this);
        ButterKnife.bind(this);

        feed.setAdapter(adapter);
        feed.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.transparent_divider));
        feed.addItemDecoration(decoration);

        presenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // we need to unregister the adapter to make sure we call onViewDetachedFromWindow(ViewHolder)
        // for the visible items.
        feed.swapAdapter(null, true);

        presenter.detachView();
    }

    @Override
    public void updateKeys(List<String> keys) {
        adapter.setKeys(keys);
    }

    @OnClick(R.id.fab)
    public void onAddBetClicked() {
        startActivity(new Intent(this, AddBetActivity.class));
    }
}
