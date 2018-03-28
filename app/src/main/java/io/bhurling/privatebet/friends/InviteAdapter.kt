package io.bhurling.privatebet.friends

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.bhurling.privatebet.R
import kotterknife.bindView

class InviteAdapter : RecyclerView.Adapter<InviteAdapter.ViewHolder>() {

    // TODO make it a diffable list
    var items: List<InviteAdapterItem> = listOf()
    set(value) {
        field = value

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_invite, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items[position].let {
            with(holder) {
                title.text = it.person.displayName
            }
        }
    }

    override fun getItemCount() = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView by bindView(R.id.title)

    }
}