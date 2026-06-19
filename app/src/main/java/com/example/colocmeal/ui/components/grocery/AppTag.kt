package com.example.colocmeal.ui.components.grocery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

enum class TagRole { MINE, SHARED, INGREDIENT_COUNT, AUTO, MANUAL, COOK }

@Composable
fun AppTag(
    label: String,
    role: TagRole,
    leadingIcon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    val container = when (role) {
        TagRole.SHARED, TagRole.COOK -> MaterialTheme.colorScheme.primaryContainer
        TagRole.MINE -> MaterialTheme.colorScheme.tertiaryContainer
        TagRole.AUTO -> MaterialTheme.colorScheme.secondaryContainer
        TagRole.MANUAL, TagRole.INGREDIENT_COUNT -> MaterialTheme.colorScheme.surfaceVariant
    }
    val content = contentColorFor(role)

    Surface(color = container, contentColor = content, shape = RoundedCornerShape(50), modifier = modifier) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            leadingIcon?.let { Icon(it, contentDescription = null, modifier = Modifier.size(14.dp)) }
            Text(label, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun contentColorFor(role: TagRole) = when (role) {
    TagRole.SHARED, TagRole.COOK -> MaterialTheme.colorScheme.onPrimaryContainer
    TagRole.MINE -> MaterialTheme.colorScheme.onTertiaryContainer
    TagRole.AUTO -> MaterialTheme.colorScheme.onSecondaryContainer
    TagRole.MANUAL, TagRole.INGREDIENT_COUNT -> MaterialTheme.colorScheme.onSurfaceVariant
}