package io.bhurling.privatebet.friends

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.bhurling.privatebet.R
import io.bhurling.privatebet.common.ui.CircleTransformation
import io.bhurling.privatebet.databinding.ItemInviteBinding
import io.bhurling.privatebet.model.pojo.Profile
import io.bhurling.privatebet.ui.diffableList
import io.bhurling.privatebet.ui.getString
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class FriendsAdapter @Inject constructor() :
    RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {

    private val _actions = MutableSharedFlow<InviteAction>()

    var items: List<FriendsItem> by diffableList(
        { old, new -> old.profile.id == new.profile.id },
        { old, new -> old == new }
    )

    fun actions() = _actions.asSharedFlow()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_invite, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], _actions)
    }

    override fun getItemCount() = items.size

    class ViewHolder(
        private val containerView: View
    ) : RecyclerView.ViewHolder(containerView) {
        private val binding = ItemInviteBinding.bind(containerView)
        private val lifecycleScope get() = containerView.findViewTreeLifecycleOwner()?.lifecycleScope

        fun bind(item: FriendsItem, actions: MutableSharedFlow<InviteAction>) {
            update(item.profile, item.isIncoming, actions)
        }

        private fun update(
            profile: Profile,
            isInvited: Boolean,
            actions: MutableSharedFlow<InviteAction>
        ) {
            profile.photoUrl?.let { url ->
                Picasso.get()
                    .load(url)
                    .error(R.drawable.default_avatar)
                    .placeholder(R.drawable.default_avatar)
                    .transform(CircleTransformation())
                    .into(binding.icon)
            } ?: binding.icon.setImageResource(R.drawable.default_avatar)

            binding.title.text = profile.displayName

            when {
                isInvited -> {
                    binding.button.visibility = View.VISIBLE
                    binding.button.text = getString(R.string.action_accept)

                    binding.button.setOnClickListener {
                        lifecycleScope?.launch {
                            actions.emit(InviteAction.Accept(profile.id))
                        }
                    }
                }

                else -> {
                    binding.button.visibility = View.GONE
                }
            }
        }
    }
}
