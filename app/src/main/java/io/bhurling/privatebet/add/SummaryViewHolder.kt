package io.bhurling.privatebet.add

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.bhurling.privatebet.R
import io.bhurling.privatebet.common.ui.CircleTransformation
import io.bhurling.privatebet.model.pojo.Person
import kotterknife.bindView

class SummaryViewHolder(val root: View) {

    private val statement: TextView by bindView(root, R.id.bets_add_summary_statement)
    private val vs: TextView by bindView(root, R.id.bets_add_summary_vs)
    private val displayName: TextView by bindView(root, R.id.title)
    private val photo: ImageView by bindView(root, R.id.icon)
    private val button: Button by bindView(root, R.id.bets_add_summary_button)

    val opponent by lazy { photo.parent as View }

    fun bind(statement: String, opponent: Person) {
        this.statement.text = statement
        this.displayName.text = opponent.displayName

        Picasso.get()
                .load(opponent.photoUrl)
                .transform(CircleTransformation())
                .into(photo)
    }

    fun fadeIn() {

        arrayOf(statement, vs).forEach {
            it.alpha = 0F
            it.animate()
                .alpha(1F)
                .setStartDelay(200)
                .withEndAction { it.alpha = 1F }
                .start()
        }

        arrayOf(button).forEach {
            it.alpha = 0F
            it.animate()
                .alpha(1F)
                .setStartDelay(300)
                .withEndAction { it.alpha = 1F }
                .start()
        }
    }
}