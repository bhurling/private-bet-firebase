package io.hurling.privatebet.core.design

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.debugInspectorInfo
import kotlinx.coroutines.delay

fun Modifier.autoFocus(
    delayInMillis: Long = 300
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "autoFocus"
    }
) {
    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(Unit) {
        delay(delayInMillis)
        focusRequester.requestFocus()
    }

    this.focusRequester(focusRequester)
}
