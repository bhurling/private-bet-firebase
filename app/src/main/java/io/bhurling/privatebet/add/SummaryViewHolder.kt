package io.bhurling.privatebet.add

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.bhurling.privatebet.R
import io.bhurling.privatebet.common.ui.CircleTransformation
import io.bhurling.privatebet.model.pojo.Person

class SummaryViewHolder(val root: View) {

    val statement by lazy {
        root.findViewById<TextView>(R.id.bets_add_summary_statement)
    }

    private val photo by lazy {
        root.findViewById<ImageView>(R.id.icon)
    }

    private val displayName by lazy {
        root.findViewById<TextView>(R.id.title)
    }

    val vs by lazy {
        root.findViewById<TextView>(R.id.bets_add_summary_vs)
    }

    val opponent by lazy {
        photo.parent as View
    }

    val button by lazy {
        root.findViewById<Button>(R.id.bets_add_summary_button)
    }

    fun bind(statement: String, opponent: Person) {
        this.statement.text = statement
        this.displayName.text = opponent.displayName

        Picasso.get()
                .load(opponent.photoUrl)
                .transform(CircleTransformation())
                .into(photo)
    }
}