package io.bhurling.privatebet.common.ui

import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView

fun RecyclerView.ViewHolder.getString(@StringRes resId: Int): String
        = itemView.context.getString(resId)
