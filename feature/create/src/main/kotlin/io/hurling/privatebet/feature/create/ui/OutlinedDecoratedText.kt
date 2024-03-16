package io.hurling.privatebet.feature.create.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedDecoratedText(
    modifier: Modifier = Modifier,
    innerText: @Composable () -> Unit,
    label: @Composable () -> Unit
) {
    val source = remember { MutableInteractionSource() }

    Box(modifier = modifier) {
        OutlinedTextFieldDefaults.DecorationBox(
            value = " ",
            innerTextField = innerText,
            enabled = true,
            singleLine = true,
            label = label,
            visualTransformation = VisualTransformation.None,
            interactionSource = source
        )
    }
}