package com.example.colocmeal.ui.grocery

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.colocmeal.domain.model.Aisle
import com.example.colocmeal.domain.model.GroceryItem
import com.example.colocmeal.domain.model.House
import com.example.colocmeal.domain.model.Source
import com.example.colocmeal.ui.components.grocery.AisleSelector
import com.example.colocmeal.ui.components.grocery.AppTag
import com.example.colocmeal.ui.components.grocery.TagRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryScreen(
    houseId: String,
    house: House?,
    viewModel: GroceryViewModel = viewModel(factory = GroceryViewModel.factory(houseId))
) {
    val items by viewModel.items.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var showHouseInfo by remember { mutableStateOf(false) }

    val total = items.size
    val bought = items.count { it.isChecked }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(house?.name ?: "Grocery") },
                actions = {
                    IconButton(onClick = { showHouseInfo = true }) {
                        Icon(Icons.Default.Info, contentDescription = "House info")
                    }
                }
            )
        },
        floatingActionButton = {
            if (items.isNotEmpty()) {
                FloatingActionButton(onClick = { showAddDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add item")
                }
            }
        }
    ) { padding ->
        if (items.isEmpty()) {
            GroceryEmptyState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                onAddItem = { showAddDialog = true }
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(bottom = 96.dp)
            ) {
                item {
                    GroceryProgress(bought = bought, total = total)
                }

                // Group by aisle, preserving the enum's logical store order.
                Aisle.entries.forEach { aisle ->
                    val aisleItems = items.filter { it.aisle == aisle }
                        .sortedBy { it.isChecked }
                    if (aisleItems.isNotEmpty()) {
                        item(key = "header-${aisle.name}") {
                            AisleHeader(
                                aisle = aisle,
                                checked = aisleItems.count { it.isChecked },
                                total = aisleItems.size
                            )
                        }
                        items(aisleItems, key = { it.id }) { item ->
                            GroceryItemRow(
                                item = item,
                                onToggle = { viewModel.toggleItemChecked(item.id, it) },
                                onDelete = { viewModel.deleteItem(item.id) }
                            )
                        }
                    }
                }
            }
        }

        if (showAddDialog) {
            AddItemDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { name, aisle ->
                    viewModel.addItem(name, aisle)
                    showAddDialog = false
                }
            )
        }

        if (showHouseInfo && house != null) {
            HouseInfoDialog(
                house = house,
                onDismiss = { showHouseInfo = false }
            )
        }
    }
}

@Composable
private fun GroceryProgress(bought: Int, total: Int) {
    val fraction = if (total == 0) 0f else bought.toFloat() / total
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$bought bought out of $total",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${(fraction * 100).toInt()}%",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { fraction },
            modifier = Modifier
                .fillMaxWidth()
                .clip(CircleShape)
        )
    }
}

@Composable
private fun AisleHeader(aisle: Aisle, checked: Int, total: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${aisle.emoji}  ${aisle.displayName}",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "$checked/$total",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun GroceryItemRow(
    item: GroceryItem,
    onToggle: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    ListItem(
        modifier = Modifier.alpha(if (item.isChecked) 0.5f else 1f),
        headlineContent = {
            Text(
                text = item.name,
                textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None
            )
        },
        leadingContent = {
            Checkbox(
                checked = item.isChecked,
                onCheckedChange = onToggle
            )
        },
        trailingContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (item.isChecked) {
                    CheckerAvatar(name = item.checkedByName)
                } else {
                    AppTag(
                        label = if (item.source == Source.AUTO) "Auto" else "Manual",
                        role = if (item.source == Source.AUTO) TagRole.AUTO else TagRole.MANUAL
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    )
}

/** Small circular avatar showing the initials of whoever checked the item. */
@Composable
private fun CheckerAvatar(name: String) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier.size(28.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = initialsOf(name),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun initialsOf(name: String): String {
    val tokens = name.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
    return when {
        tokens.isEmpty() -> "?"
        tokens.size == 1 -> tokens[0].take(1).uppercase()
        else -> (tokens.first().take(1) + tokens.last().take(1)).uppercase()
    }
}

@Composable
private fun GroceryEmptyState(
    modifier: Modifier = Modifier,
    onAddItem: () -> Unit
) {
    Column(
        modifier = modifier.padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.size(96.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
        Spacer(Modifier.height(20.dp))
        Text(
            text = "All done!",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Your grocery list is empty. New items appear automatically when you plan meals for the week.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = onAddItem) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Add item manually")
        }
    }
}

@Composable
fun HouseInfoDialog(
    house: House,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("House info") },
        text = {
            Column {
                Text("Name: ${house.name}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Invite code:")
                Text(
                    text = house.inviteCode,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Share this code with your roommates.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
fun AddItemDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, aisle: Aisle) -> Unit
) {
    var itemName by remember { mutableStateOf("") }
    var selectedAisle by remember { mutableStateOf(Aisle.OTHER) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add an item") },
        text = {
            Column {
                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = { Text("Item name") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text("Aisle", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(4.dp))
                AisleSelector(
                    selected = selectedAisle,
                    onSelect = { selectedAisle = it }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(itemName, selectedAisle) },
                enabled = itemName.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
