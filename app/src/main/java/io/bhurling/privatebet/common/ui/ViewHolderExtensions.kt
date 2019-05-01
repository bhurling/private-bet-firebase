package io.bhurling.privatebet.common.ui

import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.ViewHolder.getString(@StringRes resId: Int): String
        = itemView.context.getString(resId)
