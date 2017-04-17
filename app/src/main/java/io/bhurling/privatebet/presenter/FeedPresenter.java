package io.bhurling.privatebet.presenter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import io.bhurling.privatebet.dependencies.ReferenceFeed;

public class FeedPresenter implements ValueEventListener {

    private final DatabaseReference feed;

    private View view;

    @Inject
    public FeedPresenter(@ReferenceFeed DatabaseReference feed) {
        this.feed = feed;
    }

    public void attachView(View view) {
        this.view = view;

        this.feed.addValueEventListener(this);
    }

    public void detachView() {
        this.view = null;

        this.feed.removeEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public interface View {

    }
}
