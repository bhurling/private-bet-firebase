package io.bhurling.privatebet.feed

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.google.firebase.database.DatabaseReference

import io.bhurling.privatebet.R
import io.bhurling.privatebet.model.pojo.Bet
import io.bhurling.privatebet.rx.ReactiveFirebase
import io.reactivex.disposables.Disposable
import kotterknife.bindView

class FeedAdapter(
        private val firebase: ReactiveFirebase,
        private val bets: DatabaseReference
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val statement: TextView by bindView(R.id.statement)

        private var key: String? = null

        private var disposable: Disposable? = null

        fun bind(key: String) {
            this.key = key
        }

        fun subscribe() {
            disposable = firebase
                    .observeValueEvents(bets.child(key!!))
                    .map<Bet> { dataSnapshot -> dataSnapshot.getValue<Bet>(Bet::class.java) }
                    .subscribe({ this.update(it) }, { it.printStackTrace() })
        }

        fun unsubscribe() {
            if (disposable != null && !disposable!!.isDisposed) {
                disposable!!.dispose()
            }
        }

        fun update(bet: Bet) {
            this.statement.text = bet.statement
        }
    }
}
