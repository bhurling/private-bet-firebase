package io.bhurling.privatebet.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.bhurling.privatebet.R
import io.bhurling.privatebet.model.pojo.Bet
import io.reactivex.disposables.Disposable
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_feed.view.statement
import javax.inject.Inject

internal class FeedAdapter @Inject constructor(
        private val interactor: GetBetInteractor
) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    var keys: List<String>? = null
        set(value) {
            field = value

            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(keys!![position])
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)

        holder.unsubscribe()
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)

        holder.subscribe()
    }

    override fun getItemCount(): Int {
        return if (keys == null) 0 else keys!!.size
    }

    inner class ViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private var key: String? = null

        private var disposable: Disposable? = null

        fun bind(key: String) {
            this.key = key
        }

        fun subscribe() {
            disposable = interactor.getBet(key!!)
                    .subscribe({ this.update(it) }, { it.printStackTrace() })
        }

        fun unsubscribe() {
            disposable?.let {
                if (!it.isDisposed) it.dispose()
            }
        }

        fun update(bet: Bet) {
            containerView.statement.text = bet.statement
        }
    }
}
