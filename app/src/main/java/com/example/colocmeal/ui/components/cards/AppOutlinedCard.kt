package com.example.colocmeal.ui.components.cards

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppOutlinedCard(
    selected: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val borderColor by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
        label = "cardBorderColor"
    )
    val borderWidth by animateDpAsState(
        if (selected) 2.dp else 1.dp,
        label = "cardBorderWidth"
    )
    OutlinedCard(onClick = onClick, modifier = modifier, border = BorderStroke(borderWidth, borderColor)) {
        Column(Modifier.padding(16.dp), content = content)
    }
}