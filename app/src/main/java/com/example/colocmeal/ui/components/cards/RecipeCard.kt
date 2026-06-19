package com.example.colocmeal.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.colocmeal.domain.model.Recipe
import com.example.colocmeal.ui.components.grocery.AppTag
import com.example.colocmeal.ui.components.grocery.TagRole

@Composable
fun RecipeCard(
    recipe: Recipe,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppOutlinedCard(onClick = onClick, modifier = modifier) {
        Text(recipe.name, style = MaterialTheme.typography.titleMedium)
        recipe.description?.takeIf { it.isNotBlank() }?.let {
            Spacer(Modifier.height(4.dp))
            Text(it, style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AppTag(
                label = "${recipe.ingredients.size} ingredients",
                role = TagRole.INGREDIENT_COUNT
            )
            AppTag(
                label = if (recipe.isShared) "Shared" else "Mine",
                role = if (recipe.isShared) TagRole.SHARED else TagRole.MINE
            )
        }
        recipe.authorName.takeIf { it.isNotBlank() }?.let {
            Spacer(Modifier.height(4.dp))
            Text("by $it", style = MaterialTheme.typography.bodySmall)
        }
    }
}