package io.bhurling.privatebet.friends

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.bhurling.privatebet.R
import io.bhurling.privatebet.common.diffableList
import io.bhurling.privatebet.common.ui.CircleTransformation
import io.bhurling.privatebet.common.ui.getString
import io.bhurling.privatebet.model.pojo.Person
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import kotterknife.bindView

internal class FriendsAdapter(
        private val peopleInteractor: PeopleInteractor
) : RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {

    private val actionsSubject = PublishSubject.create<InviteAction>()

    var items: List<FriendsAdapterItem> by diffableList(
            { old, new -> old.id == new.id },
            { old, new -> old == new }
    )

    fun actions(): Observable<InviteAction> = actionsSubject

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_invite, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)

        holder.unsubscribe()
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)

        holder.subscribe()
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val icon: ImageView by bindView(R.id.icon)
        private val title: TextView by bindView(R.id.title)
        private val button: TextView by bindView(R.id.button)

        private var item: FriendsAdapterItem? = null
        private var disposable: Disposable? = null

        fun bind(item: FriendsAdapterItem) {
            this.item = item
        }

        fun subscribe() {
            item?.let { item ->
                disposable = peopleInteractor.byId(item.id)
                        .subscribe { update(it, item.isInvited) }
            }
        }

        fun unsubscribe() {
            disposable?.let {
                if (!it.isDisposed) it.dispose()
            }
        }

        private fun update(person: Person, isInvited: Boolean) {
            Picasso.get()
                    .load(person.photoUrl)
                    .transform(CircleTransformation())
                    .into(icon)

            title.text = person.displayName

            when {
                isInvited -> {
                    button.visibility = View.VISIBLE
                    button.text = getString(R.string.action_accept)

                    button.setOnClickListener {
                        actionsSubject.onNext(InviteAction.Accept(person.id))
                    }
                }
                else -> {
                    button.visibility = View.GONE
                }
            }
        }
    }
}