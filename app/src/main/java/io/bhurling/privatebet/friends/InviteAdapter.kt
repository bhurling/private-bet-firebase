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
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotterknife.bindView

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
                    else -> {
                        button.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun getItemCount() = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val icon: ImageView by bindView(R.id.icon)
        val title: TextView by bindView(R.id.title)
        val button: TextView by bindView(R.id.button)

    }
}