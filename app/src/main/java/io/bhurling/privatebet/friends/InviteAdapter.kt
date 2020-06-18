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
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_invite.view.*

class InviteAdapter : RecyclerView.Adapter<InviteAdapter.ViewHolder>() {

    private val actionsSubject = PublishSubject.create<InviteAction>()

    var items: List<InviteAdapterItem> by diffableList(
            { old, new -> old.person.id == new.person.id },
            { old, new -> old == new }
    )

    fun actions(): Observable<InviteAction> = actionsSubject

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_invite, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items[position].let { item ->
            with(holder) {
                Picasso.get()
                        .load(item.person.photoUrl)
                        .transform(CircleTransformation())
                        .into(containerView.icon)

                containerView.title.text = item.person.displayName

                when {
                    !item.isSent && !item.isIncoming -> {
                        containerView.button.visibility = View.VISIBLE
                        containerView.button.text = getString(R.string.action_add)

                        containerView.button.setOnClickListener {
                            actionsSubject.onNext(InviteAction.Invite(item.person.id))
                        }
                    }
                    item.isSent -> {
                        containerView.button.visibility = View.VISIBLE
                        containerView.button.text = getString(R.string.action_remove)

                        containerView.button.setOnClickListener {
                            actionsSubject.onNext(InviteAction.Revoke(item.person.id))
                        }
                    }
                    else -> {
                        containerView.button.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun getItemCount() = items.size

    class ViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    }
}