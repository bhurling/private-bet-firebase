package io.bhurling.privatebet.add

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import io.bhurling.privatebet.ui.doOnNextLayoutOrImmediate

class SummaryAnimator(
    private val list: RecyclerView,
    private val summary: SummaryViewHolder
) {

    private val adapter: OpponentsAdapter
        get() = list.adapter as OpponentsAdapter

    private val layoutManager: RecyclerView.LayoutManager
        get() = list.layoutManager!!

    fun show(opponentId: String) {
        if (summary.containerView.isVisible) return

        summary.containerView.visibility = View.VISIBLE

        adapter.items.indexOfFirst { it.id == opponentId }.takeUnless { it == -1 }?.let { index ->
            summary.opponent.doOnNextLayoutOrImmediate { opponent ->
                val topBefore = IntArray(2).apply {
                    layoutManager.findViewByPosition(index)
                        ?.getLocationOnScreen(this)
                }[1]

                val topAfter = IntArray(2).apply {
                    opponent.getLocationOnScreen(this)
                }[1]

                opponent.translationY = (topBefore - topAfter).toFloat()
                opponent.animate().translationY(0F).start()
            }

            summary.fadeIn()
        }
    }

    fun hide() {
        if (!summary.containerView.isVisible) return

        summary.fadeOut()
    }
}
