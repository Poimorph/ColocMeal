package com.example.colocmeal.ui.house

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.colocmeal.domain.model.House
import com.example.colocmeal.ui.components.buttons.AppButton
import com.example.colocmeal.ui.components.buttons.AppIconButton
import com.example.colocmeal.ui.components.buttons.ButtonStyle
import com.example.colocmeal.ui.components.navigation.AppTopAppBar

data class HouseMember(
    val id: String,
    val displayName: String,
)

@Composable
fun HouseDetailScreen(
    house: House?,
    members: List<HouseMember>,
    onQuitHouse: () -> Unit,
    onDisconnect: () -> Unit,
) {
    var showQuitConfirm by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "Ma Coloc",
                actions = {
                    AppIconButton(
                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Quitter la maison",
                        onClick = { showQuitConfirm = true }
                    )
                }
            )
        }
    ) { padding ->
        if (house == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = house.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Code d'invitation :", style = MaterialTheme.typography.labelLarge)
                        Text(
                            text = house.inviteCode,
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Partagez ce code pour inviter vos colocs !",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Membres de la coloc",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(members) { member ->
                        MemberItem(member)
                    }
                }

                AppButton(
                    label = "Quitter la coloc",
                    onClick = { showQuitConfirm = true },
                    modifier = Modifier.fillMaxWidth(),
                    style = ButtonStyle.OUTLINED,
                    leadingIcon = Icons.AutoMirrored.Filled.ExitToApp
                )
                Spacer(modifier = Modifier.height(8.dp))
                AppButton(
                    label = "Se déconnecter",
                    onClick = onDisconnect,
                    modifier = Modifier.fillMaxWidth(),
                    style = ButtonStyle.TONAL
                )
            }
        }
    }

    if (showQuitConfirm) {
        AlertDialog(
            onDismissRequest = { showQuitConfirm = false },
            title = { Text("Quitter la coloc ?") },
            text = { Text("Vous quitterez \"${house?.name ?: "cette coloc"}\" et aurez besoin d'un code d'invitation pour la rejoindre.") },
            confirmButton = {
                TextButton(onClick = {
                    showQuitConfirm = false
                    onQuitHouse()
                }) { Text("Quitter") }
            },
            dismissButton = {
                TextButton(onClick = { showQuitConfirm = false }) { Text("Annuler") }
            }
        )
    }
}

@Composable
private fun MemberItem(member: HouseMember) {
    ListItem(
        headlineContent = { Text(member.displayName) }
    )
}