package io.bhurling.privatebet.add

import android.view.View
import com.squareup.picasso.Picasso
import io.bhurling.privatebet.common.ui.CircleTransformation
import io.bhurling.privatebet.model.pojo.Person
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_invite.view.*
import kotlinx.android.synthetic.main.partial_add_summary.view.*

class SummaryViewHolder(override val containerView: View) : LayoutContainer {

    val opponent by lazy {
        containerView.icon.parent as View
    }

    fun bind(statement: String, opponent: Person) {
        containerView.bets_add_summary_statement.text = statement
        containerView.title.text = opponent.displayName

        Picasso.get()
                .load(opponent.photoUrl)
                .transform(CircleTransformation())
                .into(containerView.icon)
    }

    fun fadeIn() {

        arrayOf(
            containerView.bets_add_summary_statement,
            containerView.bets_add_summary_vs
        ).forEach {
            it.alpha = 0F
            it.animate()
                .alpha(1F)
                .setStartDelay(200)
                .withEndAction { it.alpha = 1F }
                .start()
        }

        arrayOf(
            containerView.bets_add_summary_button
        ).forEach {
            it.alpha = 0F
            it.animate()
                .alpha(1F)
                .setStartDelay(300)
                .withEndAction { it.alpha = 1F }
                .start()
        }
    }
}