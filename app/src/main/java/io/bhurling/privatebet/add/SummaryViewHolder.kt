package io.bhurling.privatebet.add

import android.view.View
import androidx.core.view.isVisible
import com.squareup.picasso.Picasso
import io.bhurling.privatebet.R
import io.bhurling.privatebet.common.ui.CircleTransformation
import io.bhurling.privatebet.databinding.PartialAddSummaryBinding
import io.bhurling.privatebet.model.pojo.Person

class SummaryViewHolder(val containerView: View) {
    private val binding = PartialAddSummaryBinding.bind(containerView)

    val opponent by lazy {
        binding.itemOpponentInclude.icon.parent as View
    }

    fun bind(statement: String, opponent: Person) {
        binding.betsAddSummaryStatement.text = statement
        binding.itemOpponentInclude.title.text = opponent.displayName

        opponent.photoUrl?.let { url ->
            Picasso.get()
                .load(url)
                .error(R.drawable.default_avatar)
                .placeholder(R.drawable.default_avatar)
                .transform(CircleTransformation())
                .into(binding.itemOpponentInclude.icon)
        } ?: binding.itemOpponentInclude.icon.setImageResource(R.drawable.default_avatar)
    }

    fun fadeIn() {
        arrayOf(
            binding.betsAddSummaryStatement,
            binding.betsAddSummaryVs
        ).forEach {
            it.alpha = 0F
            it.animate()
                .alpha(1F)
                .setStartDelay(200)
                .withEndAction { it.alpha = 1F }
                .start()
        }

        arrayOf(
            binding.betsAddSummaryButton
        ).forEach {
            it.alpha = 0F
            it.animate()
                .alpha(1F)
                .setStartDelay(300)
                .withEndAction { it.alpha = 1F }
                .start()
        }
    }

    fun fadeOut() {
        containerView.animate()
            .alpha(0F)
            .withEndAction {
                containerView.isVisible = false
                containerView.alpha = 1F
            }
    }
}