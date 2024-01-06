package io.bhurling.privatebet.add

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.bhurling.privatebet.R
import io.bhurling.privatebet.common.ui.CircleTransformation
import io.bhurling.privatebet.databinding.ItemOpponentBinding
import io.bhurling.privatebet.ui.diffableList
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class OpponentsAdapter @Inject constructor() : RecyclerView.Adapter<OpponentsAdapter.ViewHolder>() {

    private val actionsSubject = PublishSubject.create<OpponentsAction>()

    var items: List<OpponentsAdapterItem> by diffableList(
        { old, new -> old.profile.id == new.profile.id },
        { old, new -> old == new }
    )

    fun actions(): Observable<OpponentsAction> = actionsSubject

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_opponent, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(
        containerView: View
    ) : RecyclerView.ViewHolder(containerView) {
        private val binding = ItemOpponentBinding.bind(containerView)

        fun bind(item: OpponentsAdapterItem) {
            itemView.setOnClickListener {
                actionsSubject.onNext(OpponentsAction.Selected(item.profile))
            }

            item.profile.photoUrl?.let { url ->
                Picasso.get()
                    .load(url)
                    .error(R.drawable.default_avatar)
                    .placeholder(R.drawable.default_avatar)
                    .transform(CircleTransformation())
                    .into(binding.icon)
            } ?: binding.icon.setImageResource(R.drawable.default_avatar)

            binding.title.text = item.profile.displayName
        }
    }
}
