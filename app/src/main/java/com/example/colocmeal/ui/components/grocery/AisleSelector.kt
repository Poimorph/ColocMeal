package com.example.colocmeal.ui.components.grocery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.colocmeal.domain.model.Aisle

/**
 * Grid of aisle filter chips, shared by the grocery and recipe add dialogs.
 * Built from plain Row/Column (no FlowRow) to stay compatible across Compose versions.
 */
@Composable
fun AisleSelector(
    selected: Aisle,
    onSelect: (Aisle) -> Unit,
    columns: Int = 2,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Aisle.entries.chunked(columns).forEach { rowAisles ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                rowAisles.forEach { aisle ->
                    AppChip(
                        label = "${aisle.emoji} ${aisle.displayName}",
                        kind = ChipKind.FILTER,
                        selected = aisle == selected,
                        onClick = { onSelect(aisle) },
                        modifier = Modifier.weight(1f)
                    )
                }
                // Pad the last row so chips keep a consistent width.
                repeat(columns - rowAisles.size) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}