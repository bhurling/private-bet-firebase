package io.bhurling.privatebet.presenter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.bhurling.privatebet.dependencies.FeedReference;
import io.bhurling.privatebet.rx.ReactiveFirebase;
import io.reactivex.Observable;

public class FeedPresenter extends Presenter<FeedPresenter.View> {

    private final ReactiveFirebase firebase;
    private final DatabaseReference feed;

    @Inject
    public FeedPresenter(ReactiveFirebase firebase, @FeedReference DatabaseReference feed) {
        this.firebase = firebase;
        this.feed = feed;
    }

    @Override
    public void attachView(View view) {
        super.attachView(view);

        disposables.addAll(
                firebase.observeValueEvents(this.feed.orderByValue())
                        .map(DataSnapshot::getChildren)
                        .map(this::keys)
                        .map(this::reverse)
                        .subscribe(this::handleData)
        );
    }

    private void handleData(List<String> keys) {
        view.updateKeys(keys);
    }

    private List<String> keys(Iterable<DataSnapshot> data) {
        return Observable.fromIterable(data)
                .map(DataSnapshot::getKey)
                .toList()
                .blockingGet();
    }

    private List<String> reverse(List<String> list) {
        Collections.reverse(list);

        return list;
    }

    public interface View extends Presenter.View {
        void updateKeys(List<String> keys);
    }
}
