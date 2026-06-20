package com.example.colocmeal.ui.components.selection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

enum class ChipKind {
    ASSIST,
    FILTER,
    INPUT,
    CATEGORY
}

@Composable
fun AppChip(
    label: String,
    kind: ChipKind = ChipKind.FILTER,
    selected: Boolean = false,
    onClick: () -> Unit = {},
    leadingIcon: ImageVector? = null,
    onDismiss: (() -> Unit)? = null
) {
    when (kind) {
        ChipKind.ASSIST -> {
            AssistChip(
                onClick = onClick,
                label = {
                    Text(text = label)
                },
                leadingIcon = leadingIcon?.let { icon ->
                    {
                        Icon(
                            imageVector = icon,
                            contentDescription = null
                        )
                    }
                }
            )
        }

        ChipKind.FILTER -> {
            FilterChip(
                selected = selected,
                onClick = onClick,
                label = {
                    Text(text = label)
                },
                leadingIcon = leadingIcon?.let { icon ->
                    {
                        Icon(
                            imageVector = icon,
                            contentDescription = null
                        )
                    }
                }
            )
        }

        ChipKind.INPUT -> {
            InputChip(
                selected = selected,
                onClick = onClick,
                label = {
                    Text(text = label)
                },
                leadingIcon = leadingIcon?.let { icon ->
                    {
                        Icon(
                            imageVector = icon,
                            contentDescription = null
                        )
                    }
                },
                trailingIcon = onDismiss?.let { dismiss ->
                    {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Supprimer $label",
                            modifier = Modifier
                                .size(18.dp)
                                .clickable(onClick = dismiss)
                        )
                    }
                }
            )
        }

        ChipKind.CATEGORY -> {
            SuggestionChip(
                onClick = onClick,
                label = {
                    Text(text = label)
                },
                icon = leadingIcon?.let { icon ->
                    {
                        Icon(
                            imageVector = icon,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    }
}