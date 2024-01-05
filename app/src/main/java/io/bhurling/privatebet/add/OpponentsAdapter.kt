package io.bhurling.privatebet.add

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import io.bhurling.privatebet.R
import io.bhurling.privatebet.common.ui.CircleTransformation
import io.bhurling.privatebet.databinding.ItemOpponentBinding
import io.bhurling.privatebet.model.pojo.Profile
import io.bhurling.privatebet.model.toPerson
import io.bhurling.privatebet.rx.firebase.ReactiveFirebase
import io.bhurling.privatebet.ui.diffableList
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class OpponentsAdapter @Inject constructor(
    private val firebase: ReactiveFirebase,
    private val store: FirebaseFirestore,
) : RecyclerView.Adapter<OpponentsAdapter.ViewHolder>() {

    private val actionsSubject = PublishSubject.create<OpponentsAction>()

    var items: List<OpponentsAdapterItem> by diffableList(
        { old, new -> old.id == new.id },
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

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)

        holder.unsubscribe()
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)

        holder.subscribe()
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(
        private val containerView: View
    ) : RecyclerView.ViewHolder(containerView) {
        private val binding = ItemOpponentBinding.bind(containerView)

        private var _item: OpponentsAdapterItem? = null
        private var disposable: Disposable? = null

        fun bind(item: OpponentsAdapterItem) {
            this._item = item
        }

        fun subscribe() {
            _item?.let { item ->
                disposable = firebase
                    .observeValueEvents(store.profiles.document(item.id))
                    .map { it.toPerson() }
                    .subscribe { update(it) }
            }
        }

        fun unsubscribe() {
            disposable?.let {
                if (!it.isDisposed) it.dispose()
            }
        }

        private fun update(profile: Profile) {
            itemView.setOnClickListener {
                actionsSubject.onNext(OpponentsAction.Selected(profile))
            }

            profile.photoUrl?.let { url ->
                Picasso.get()
                    .load(url)
                    .error(R.drawable.default_avatar)
                    .placeholder(R.drawable.default_avatar)
                    .transform(CircleTransformation())
                    .into(binding.icon)
            } ?: binding.icon.setImageResource(R.drawable.default_avatar)

            binding.title.text = profile.displayName
        }
    }
}

private val FirebaseFirestore.profiles get() = collection("public_profiles")