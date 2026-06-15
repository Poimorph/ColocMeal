package com.example.colocmeal.ui.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.colocmeal.ui.components.buttons.AppButton
import com.example.colocmeal.ui.components.inputs.AppTextField

@Composable
fun SignUpScreen(
    onNavigateToSignIn: () -> Unit,
    viewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory),) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column() {
        AppTextField(uiState.displayName, viewModel::onDisplayNameChange, "Display Name")
        AppTextField(uiState.email, viewModel::onEmailChange, "Email")
        AppTextField(uiState.password, viewModel::onPasswordChange, label = "Password", visualTransformation = PasswordVisualTransformation())
        uiState.error?.let{ Text(it, color = MaterialTheme.colorScheme.error) }
        AppButton("Sign up", onClick = viewModel::submitSignUp, enabled =  !uiState.isLoading)
        AppButton("Already have an account ? Sign In", onNavigateToSignIn)
    }
}