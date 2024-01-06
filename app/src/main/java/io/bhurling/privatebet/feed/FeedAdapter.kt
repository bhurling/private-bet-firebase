package io.bhurling.privatebet.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.bhurling.privatebet.R
import javax.inject.Inject

internal class FeedAdapter @Inject constructor() : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    var keys: List<String>? = null
        set(value) {
            field = value

            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(keys!![position])
    }

    override fun getItemCount(): Int {
        return if (keys == null) 0 else keys!!.size
    }

    inner class ViewHolder(
        private val containerView: View
    ) : RecyclerView.ViewHolder(containerView) {
        private var key: String? = null

        fun bind(key: String) {
            this.key = key
        }
    }
}
