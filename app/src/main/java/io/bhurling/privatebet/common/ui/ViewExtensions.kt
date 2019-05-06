package io.bhurling.privatebet.common.ui

import android.view.View
import androidx.core.view.doOnNextLayout

inline fun View.doOnNextLayoutOrImmediate(crossinline action: (view: View) -> Unit) {
    if (isLaidOut) {
        action(this)
    } else {
        doOnNextLayout(action)
    }
}
