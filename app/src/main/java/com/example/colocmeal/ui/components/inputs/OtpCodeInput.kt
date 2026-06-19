package com.example.colocmeal.ui.components.inputs

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

enum class CodeBoxState { EMPTY, CURSOR, FILLED }

@Composable
fun OtpCodeInput(
    value: String,
    onValueChange: (String) -> Unit,
    length: Int = 6,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = { if (it.length <= length) onValueChange(it.uppercase()) },
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(length) { i ->
                    val state = when {
                        i < value.length      -> CodeBoxState.FILLED
                        i == value.length     -> CodeBoxState.CURSOR
                        else                  -> CodeBoxState.EMPTY
                    }
                    CodeBox(value.getOrNull(i), state)
                }
            }
        }
    )
}

@Composable
private fun CodeBox(char: Char?, state: CodeBoxState) {
    val border = when (state) {
        CodeBoxState.CURSOR -> MaterialTheme.colorScheme.primary
        else                -> MaterialTheme.colorScheme.outline
    }
    Box(
        Modifier.size(44.dp).border(1.dp, border, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(char?.toString() ?: "", style = MaterialTheme.typography.titleLarge)
    }
}