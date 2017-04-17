package io.bhurling.privatebet.presenter;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.bhurling.privatebet.dependencies.ReferenceFeed;
import io.bhurling.privatebet.rx.RxFirebaseDatabase;
import io.reactivex.Observable;

public class FeedPresenter extends Presenter<FeedPresenter.View> {

    private final DatabaseReference feed;

    @Inject
    public FeedPresenter(@ReferenceFeed DatabaseReference feed) {
        this.feed = feed;
    }

    @Override
    public void attachView(View view) {
        super.attachView(view);

        disposables.addAll(
                RxFirebaseDatabase.observeValueEvents(this.feed.orderByValue())
                        .map(DataSnapshot::getChildren)
                        .map(this::makeList)
                        .map(this::reverse)
                        .subscribe(this::handleData)
        );
    }

    private void handleData(List<DataSnapshot> list) {
        for (DataSnapshot item : list) {
            Log.d("FEED", item.toString());
        }
    }

    private List<DataSnapshot> makeList(Iterable<DataSnapshot> data) {
        return Observable.fromIterable(data).toList().blockingGet();
    }

    private List<DataSnapshot> reverse(List<DataSnapshot> list) {
        Collections.reverse(list);

        return list;
    }

    public interface View extends Presenter.View {

    }
}
