package io.bhurling.privatebet.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import io.bhurling.privatebet.R;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private List<String> keys;

    @Inject
    public FeedAdapter() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

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
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
