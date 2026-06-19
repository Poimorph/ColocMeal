package com.example.colocmeal.ui.components.grocery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

enum class ChipKind { ASSIST, FILTER, INPUT, CATEGORY }

@Composable
fun AppChip(
    label: String,
    kind: ChipKind = ChipKind.FILTER,
    selected: Boolean = false,
    onClick: () -> Unit = {},
    leadingIcon: ImageVector? = null,
    onDismiss: (() -> Unit)? = null // only INPUT
) {
    val leading: (@Composable () -> Unit)? = leadingIcon?.let {
        { Icon(it, contentDescription = null, modifier = Modifier.size(FilterChipDefaults.IconSize)) }
    }
    when (kind) {
        ChipKind.INPUT -> InputChip(
            selected = selected,
            onClick = onClick,
            label = { Text(label) },
            leadingIcon = leading,
            trailingIcon = onDismiss?.let {
                {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove $label",
                        modifier = Modifier
                            .size(InputChipDefaults.AvatarSize)
                            .clickable(onClick = it)
                    )
                }
            }
        )

        ChipKind.FILTER -> FilterChip(
            selected = selected,
            onClick = onClick,
            label = { Text(label) },
            leadingIcon = leading
        )

        ChipKind.ASSIST -> AssistChip(
            onClick = onClick,
            label = { Text(label) },
            leadingIcon = leading
        )

        ChipKind.CATEGORY -> SuggestionChip(
            onClick = onClick,
            label = { Text(label) }
        )
    }
}