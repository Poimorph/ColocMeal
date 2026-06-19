package com.example.colocmeal.ui.grocery

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.colocmeal.domain.model.Aisle
import com.example.colocmeal.domain.model.GroceryItem
import com.example.colocmeal.domain.model.House
import com.example.colocmeal.ui.components.grocery.AppChip
import com.example.colocmeal.ui.components.grocery.ChipKind

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(house?.name ?: "Liste de courses") },
                actions = {
                    IconButton(onClick = { showHouseInfo = true }) {
                        Icon(Icons.Default.Info, contentDescription = "Infos Maison")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(items) { item ->
                GroceryItemRow(
                    item = item,
                    onToggle = { viewModel.toggleItemChecked(item.id, it) },
                    onDelete = { viewModel.deleteItem(item.id) }
                )
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
fun HouseInfoDialog(
    house: House,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Informations de la Coloc") },
        text = {
            Column {
                Text("Nom : ${house.name}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Code d'invitation :")
                Text(
                    text = house.inviteCode,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Partagez ce code avec vos colocataires.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Fermer")
            }
        }
    )
}

@Composable
fun GroceryItemRow(
    item: GroceryItem,
    onToggle: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    ListItem(
        headlineContent = { Text(item.name) },
        leadingContent = {
            Checkbox(
                checked = item.isChecked,
                onCheckedChange = onToggle
            )
        },
        trailingContent = {
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Supprimer")
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
        title = { Text("Ajouter un article") },
        text = {
            Column {
                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = { Text("Nom de l'article") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text("Rayon", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Aisle.entries.forEach { aisle ->
                        AppChip(
                            label = "${aisle.emoji} ${aisle.displayName}",
                            kind = ChipKind.FILTER,
                            selected = aisle == selectedAisle,
                            onClick = { selectedAisle = aisle }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(itemName, selectedAisle) },
                enabled = itemName.isNotBlank()
            ) {
                Text("Ajouter")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}
