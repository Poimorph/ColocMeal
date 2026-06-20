package com.example.colocmeal.ui.components.inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    isError: Boolean = false,
    trailingIcon: ImageVector? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLines: Int = Int.MAX_VALUE
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(text = label) },
        supportingText = supportingText?.let { text ->
            { Text(text = text) }
        },
        isError = isError,
        trailingIcon = trailingIcon?.let { icon ->
            {
                Icon(
                    imageVector = icon,
                    contentDescription = null
                )
            }
        },
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        maxLines = maxLines
    )
}

@Composable
fun AppOtpField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    length: Int = 6,
    enabled: Boolean = true,
    isError: Boolean = false
) {
    val otpValue = value
        .filter { it.isDigit() }
        .take(length)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(length) { index ->
            val digit = otpValue.getOrNull(index)?.toString() ?: ""

            OutlinedTextField(
                value = digit,
                onValueChange = { input ->
                    val digits = input.filter { it.isDigit() }

                    if (digits.length > 1) {
                        // Permet aussi de coller directement un code complet.
                        onValueChange(digits.take(length))
                    } else {
                        val currentValue = otpValue.toMutableList()

                        if (digits.isNotEmpty()) {
                            if (index < currentValue.size) {
                                currentValue[index] = digits.first()
                            } else if (currentValue.size < length) {
                                currentValue.add(digits.first())
                            }
                        } else if (index < currentValue.size) {
                            currentValue.removeAt(index)
                        }

                        onValueChange(
                            currentValue
                                .joinToString("")
                                .take(length)
                        )
                    }
                },
                modifier = Modifier.width(48.dp),
                enabled = enabled,
                isError = isError,
                singleLine = true,
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    textAlign = TextAlign.Center
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword
                )
            )
        }
    }
}