package io.bhurling.privatebet.common.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun Theme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF3D5065),
            secondary = Color(0xFFFF6D28)
        )
    ) {
        content()
    }
}