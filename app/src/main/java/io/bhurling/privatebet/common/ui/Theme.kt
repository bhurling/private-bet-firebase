package io.bhurling.privatebet.common.ui

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun Theme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF3D5065),
            onPrimary = Color(0xFFFFFFFF),
            secondary = Color(0xFFFF6D28),
            onSecondary = Color(0xFFFFFFFF),
            onBackground = Color(0xFF3D5065)
        )
    ) {
        CompositionLocalProvider(LocalContentColor provides Color(0xFF3D5065)) {
            ProvideTextStyle(
                value = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                content = content
            )
        }
    }
}
