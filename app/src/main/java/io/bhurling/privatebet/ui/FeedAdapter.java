package io.bhurling.privatebet.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.bhurling.privatebet.R;
import io.bhurling.privatebet.dependencies.BetsReference;
import io.bhurling.privatebet.model.pojo.Bet;
import io.bhurling.privatebet.rx.ReactiveFirebase;
import io.reactivex.disposables.Disposable;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private final ReactiveFirebase firebase;
    private final DatabaseReference bets;

    private List<String> keys;

    @Inject
    public FeedAdapter(ReactiveFirebase firebase, @BetsReference DatabaseReference bets) {
        this.firebase = firebase;
        this.bets = bets;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(keys.get(position));
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        holder.unsubscribe();
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        holder.subscribe();
    }

    @Override
    public int getItemCount() {
        return keys == null ? 0 : keys.size();
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;

        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.statement)
        TextView statement;

        private String key;

        private Disposable disposable;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bind(String key) {
            this.key = key;
        }

        private void subscribe() {
            disposable = firebase
                    .observeValueEvents(bets.child(key))
                    .map(dataSnapshot -> dataSnapshot.getValue(Bet.class))
                    .subscribe(this::update, Throwable::printStackTrace);
        }

        public void unsubscribe() {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }

        public void update(Bet bet) {
            this.statement.setText(bet.statement);
        }
    }
}
