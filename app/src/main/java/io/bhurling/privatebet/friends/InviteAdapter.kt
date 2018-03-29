package io.bhurling.privatebet.friends

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.bhurling.privatebet.R
import io.bhurling.privatebet.common.ui.CircleTransformation
import io.bhurling.privatebet.common.ui.getString
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotterknife.bindView

class InviteAdapter : RecyclerView.Adapter<InviteAdapter.ViewHolder>() {

    private val actionsSubject = PublishSubject.create<InviteAction>()

    // TODO make it a diffable list
    var items: List<InviteAdapterItem> = listOf()
        set(value) {
            field = value

            notifyDataSetChanged()
        }

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
                        .into(icon)

                title.text = item.person.displayName

                when {
                    !item.isSent && !item.isIncoming -> {
                        button.visibility = View.VISIBLE
                        button.text = getString(R.string.action_add)

                        button.setOnClickListener {
                            actionsSubject.onNext(InviteAction.Invite(item.person.id))
                        }
                    }
                    item.isSent -> {
                        button.visibility = View.VISIBLE
                        button.text = getString(R.string.action_remove)

                        button.setOnClickListener {
                            actionsSubject.onNext(InviteAction.Revoke(item.person.id))
                        }
                    }
                    item.isIncoming -> {
                        button.visibility = View.VISIBLE
                        button.text = getString(R.string.action_accept)

                        button.setOnClickListener {
                            actionsSubject.onNext(InviteAction.Accept(item.person.id))
                        }
                    }
                    else -> {
                        button.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun getItemCount() = items.size

    private fun canInvite(item: InviteAdapterItem) = !item.isSent && !item.isIncoming

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val icon: ImageView by bindView(R.id.icon)
        val title: TextView by bindView(R.id.title)
        val button: TextView by bindView(R.id.button)

    }
}