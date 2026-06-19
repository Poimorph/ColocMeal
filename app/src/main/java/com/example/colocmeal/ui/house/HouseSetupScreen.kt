package com.example.colocmeal.ui.house

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.colocmeal.ui.components.AppSegmentedButton
import com.example.colocmeal.ui.components.buttons.AppButton
import com.example.colocmeal.ui.components.cards.AppOutlinedCard
import com.example.colocmeal.ui.components.inputs.AppTextField
import com.example.colocmeal.ui.components.inputs.OtpCodeInput

@Composable
fun HouseSetupScreen(
    viewModel: HouseSetupViewModel = viewModel(factory = HouseSetupViewModel.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val isCreate = state.mode == SetupMode.CREATE

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(Modifier.height(10.dp))
        // Header
        Text("ColocMeal", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text("Your house", style = MaterialTheme.typography.headlineMedium)


        Spacer(Modifier.height(8.dp))

        // Mode toggle
        AppSegmentedButton(
            options = listOf(SetupMode.CREATE, SetupMode.JOIN),
            selected = state.mode,
            onSelect = viewModel::onModeChange,
            labelOf = { if (it == SetupMode.CREATE) "Create" else "Join" }
        )

        // Create card
        val createAlpha by animateFloatAsState(if (isCreate) 1f else 0.55f, label = "createAlpha")
        AppOutlinedCard(
            selected = isCreate,
            onClick = { viewModel.onModeChange(SetupMode.CREATE) },
            modifier = Modifier
                .fillMaxWidth()
                .alpha(createAlpha)
        ) {
            CardHeader(Icons.Filled.Home, "Create a house", "You will be administrator", isCreate)
            Spacer(Modifier.height(12.dp))
            AppTextField(state.houseName, viewModel::onHouseNameChange, "House name")
        }

        // Join card
        val joinAlpha by animateFloatAsState(if (!isCreate) 1f else 0.55f, label = "joinAlpha")
        AppOutlinedCard(
            selected = !isCreate,
            onClick = { viewModel.onModeChange(SetupMode.JOIN) },
            modifier = Modifier
                .fillMaxWidth()
                .alpha(joinAlpha)
        ) {
            CardHeader(Icons.AutoMirrored.Filled.ExitToApp, "Join a house", "Enter your roommate's code", !isCreate)
            Spacer(Modifier.height(12.dp))
            OtpCodeInput(state.inviteCodeInput, viewModel::onInviteCodeChange)
        }

        // Contextual info banner
        AnimatedVisibility(
            visible = isCreate,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            InfoBanner("An invitation code will be generated to invite your roommates.")
        }

        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        Spacer(Modifier.height(8.dp))
        AppButton(
            "Continue",
            onClick = viewModel::submit,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/** Rounded square icon badge */
@Composable
private fun IconBadge(icon: ImageVector, active: Boolean, size: androidx.compose.ui.unit.Dp = 40.dp) {
    val bg by animateColorAsState(
        if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainer,
        label = "badgeBg"
    )
    val fg by animateColorAsState(
        if (active) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
        label = "badgeFg"
    )
    Box(
        Modifier
            .size(size)
            .background(bg, RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = fg)
    }
}

@Composable
private fun CardHeader(icon: ImageVector, title: String, subtitle: String, active: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        IconBadge(icon, active)
        Column {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun InfoBanner(text: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.tertiaryContainer, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(Icons.Filled.Info, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
        Text(text, style = MaterialTheme.typography.bodySmall, color = Color(0xFF1C4D33))
    }
}

