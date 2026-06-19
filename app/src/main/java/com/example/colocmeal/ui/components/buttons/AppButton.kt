package com.example.colocmeal.ui.components.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon

enum class ButtonStyle { FILLED, TONAL, OUTLINED, TEXT, ELEVATED }

@Composable
fun AppButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: ButtonStyle = ButtonStyle.FILLED,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true
){
    Button(onClick=onClick,modifier=modifier, enabled=enabled) {
        if (enabled){
            Text(label)
        }else{
            CircularProgressIndicator(Modifier.size(16.dp))
        }
    }
}

@Composable
fun AppFab(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}