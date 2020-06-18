package io.bhurling.privatebet.friends

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.bhurling.privatebet.R
import io.bhurling.privatebet.common.diffableList
import io.bhurling.privatebet.common.ui.CircleTransformation
import io.bhurling.privatebet.common.ui.getString
import io.bhurling.privatebet.model.pojo.Person
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_invite.view.*

internal class FriendsAdapter : RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {

    private val actionsSubject = PublishSubject.create<InviteAction>()

    var items: List<FriendsAdapterItem> by diffableList(
        { old, new -> old.person.id == new.person.id },
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

    override fun getItemCount() = items.size

    inner class ViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(item: FriendsAdapterItem) {
            update(item.person, item.isInvited)
        }

        private fun update(person: Person, isInvited: Boolean) {
            Picasso.get()
                .load(person.photoUrl)
                .error(R.drawable.default_avatar)
                .transform(CircleTransformation())
                .into(containerView.icon)

            containerView.title.text = person.displayName

            when {
                isInvited -> {
                    containerView.button.visibility = View.VISIBLE
                    containerView.button.text = getString(R.string.action_accept)

                    containerView.button.setOnClickListener {
                        actionsSubject.onNext(InviteAction.Accept(person.id))
                    }
                }
                else -> {
                    containerView.button.visibility = View.GONE
                }
            }
        }
    }
}