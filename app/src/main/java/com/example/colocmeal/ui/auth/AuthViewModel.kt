package com.example.colocmeal.ui.auth

import android.util.Patterns
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.colocmeal.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.colocmeal.ui.container


data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val displayName: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
)

class AuthViewModel(
    private val authRepository: AuthRepository
): ViewModel(){

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState : StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String)       = _uiState.update { it.copy(email = value, error = null) }
    fun onPasswordChange(value: String)    = _uiState.update { it.copy(password = value, error = null) }
    fun onDisplayNameChange(value: String) = _uiState.update { it.copy(displayName = value, error = null) }

    fun submitSignIn() {
        val s = _uiState.value
        validate(s, requireName = false)?.let { msg ->
            _uiState.update { it.copy(error = msg) }; return
        }
        launch { authRepository.signIn(s.email.trim(), s.password) }
    }

    fun submitSignUp() {
        val s = _uiState.value
        validate(s, requireName = true)?.let { msg ->
            _uiState.update { it.copy(error = msg) }; return
        }
        launch { authRepository.signUp(s.email.trim(), s.password, s.displayName.trim()) }
    }

    /** Runs an auth call with loading/error handling. On success, do nothing —
     *  the auth-state listener (3.4) re-routes the app. */
    private fun launch(block: suspend () -> Result<String>) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = block()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message,  // null on success
                )
            }
        }
    }

    /** Returns an error message, or null if valid. */
    private fun validate(s: AuthUiState, requireName: Boolean): String? = when {
        !Patterns.EMAIL_ADDRESS.matcher(s.email.trim()).matches() -> "Enter a valid email."
        s.password.length < 6 -> "Password must be at least 6 characters."
        requireName && s.displayName.isBlank() -> "Enter a display name."
        else -> null
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { AuthViewModel(container().authRepository) }
        }
    }
}