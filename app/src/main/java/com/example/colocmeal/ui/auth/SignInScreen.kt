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
fun SignInScreen(
    onNavigateToSignUp: () -> Unit,
    viewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory),) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column() {
        AppTextField(uiState.email, viewModel::onEmailChange, "Email")
        AppTextField(uiState.password, viewModel::onPasswordChange, label = "Password", visualTransformation = PasswordVisualTransformation())
        uiState.error?.let{ Text(it, color = MaterialTheme.colorScheme.error) }
        AppButton("Sign in", onClick = viewModel::submitSignIn, enabled =  !uiState.isLoading)
        AppButton("Need an account ? Sign Up", onNavigateToSignUp)
    }
}