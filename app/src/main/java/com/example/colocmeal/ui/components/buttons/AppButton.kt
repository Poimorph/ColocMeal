package com.example.colocmeal.ui.components.buttons

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon

enum class ButtonStyle {
    FILLED,
    TONAL,
    OUTLINED,
    TEXT,
    ELEVATED
}

@Composable
fun AppButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: ButtonStyle = ButtonStyle.FILLED,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true
) {
    when (style) {
        ButtonStyle.FILLED -> {
            Button(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled
            ) {
                ButtonContent(label, leadingIcon, enabled)
            }
        }

        ButtonStyle.TONAL -> {
            FilledTonalButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled
            ) {
                ButtonContent(label, leadingIcon, enabled)
            }
        }

        ButtonStyle.OUTLINED -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled
            ) {
                ButtonContent(label, leadingIcon, enabled)
            }
        }

        ButtonStyle.TEXT -> {
            TextButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled
            ) {
                ButtonContent(label, leadingIcon, enabled)
            }
        }

        ButtonStyle.ELEVATED -> {
            ElevatedButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled
            ) {
                ButtonContent(label, leadingIcon, enabled)
            }
        }
    }
}

@Composable
private fun ButtonContent(
    label: String,
    leadingIcon: ImageVector?,
    enabled: Boolean
) {
    if (!enabled) {
        CircularProgressIndicator(
            modifier = Modifier.size(16.dp),
            strokeWidth = 2.dp
        )
    } else {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
        }

        Text(text = label)
    }
}

@Composable
fun AppFab(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}