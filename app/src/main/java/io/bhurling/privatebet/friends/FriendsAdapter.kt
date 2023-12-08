package io.bhurling.privatebet.friends

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.bhurling.privatebet.R
import io.bhurling.privatebet.common.ui.CircleTransformation
import io.bhurling.privatebet.databinding.ItemInviteBinding
import io.bhurling.privatebet.model.pojo.Person
import io.bhurling.privatebet.ui.diffableList
import io.bhurling.privatebet.ui.getString
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

internal class FriendsAdapter @Inject constructor() :
    RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {

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
        holder.bind(items[position], actionsSubject)
    }

    override fun getItemCount() = items.size

    class ViewHolder(
        private val containerView: View
    ) : RecyclerView.ViewHolder(containerView) {
        private val binding = ItemInviteBinding.bind(containerView)

        fun bind(item: FriendsAdapterItem, actions: Observer<InviteAction>) {
            update(item.person, item.isInvited, actions)
        }

        private fun update(
            person: Person,
            isInvited: Boolean,
            actions: Observer<InviteAction>
        ) {
            Picasso.get()
                .load(person.photoUrl)
                .error(R.drawable.default_avatar)
                .transform(CircleTransformation())
                .into(binding.icon)

            binding.title.text = person.displayName

            when {
                isInvited -> {
                    binding.button.visibility = View.VISIBLE
                    binding.button.text = getString(R.string.action_accept)

                    binding.button.setOnClickListener {
                        actions.onNext(InviteAction.Accept(person.id))
                    }
                }

                else -> {
                    binding.button.visibility = View.GONE
                }
            }
        }
    }
}