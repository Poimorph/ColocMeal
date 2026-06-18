package com.example.colocmeal.ui.house

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.colocmeal.domain.model.House
import com.example.colocmeal.domain.repository.AuthRepository
import com.example.colocmeal.domain.repository.HouseRepository
import com.example.colocmeal.domain.repository.UserRepository
import com.example.colocmeal.ui.container
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

const val MAX_ATTEMPTS = 10

enum class SetupMode { CREATE, JOIN }

data class HouseSetupUiState(
    val mode: SetupMode = SetupMode.CREATE,
    val houseName: String = "",
    val inviteCodeInput: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class HouseSetupViewModel(
    private val houseRepository: HouseRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HouseSetupUiState())
    val uiState: StateFlow<HouseSetupUiState> = _uiState.asStateFlow()

    fun onHouseNameChange(value: String) =
        _uiState.update { it.copy(houseName = value, error = null) }

    fun onInviteCodeChange(value: String) =
        _uiState.update { it.copy(inviteCodeInput = value, error = null) }

    fun onModeChange(mode: SetupMode) =
        _uiState.update { it.copy(mode = mode, error = null) }

    /** Single "Continue" action — dispatches on the selected mode. */
    fun submit() = when (_uiState.value.mode) {
        SetupMode.CREATE -> createHouse()
        SetupMode.JOIN   -> joinHouse()
    }

    fun createHouse() {
        val name = _uiState.value.houseName.trim()
        if (name.isBlank()) {
            _uiState.update { it.copy(error = "Enter a house name.") }
            return
        }
        val uid = authRepository.currentUid ?: run {
            _uiState.update { it.copy(error = "Not signed in.") }
            return
        }
        launch {
            requireNoHouse(uid)
            val code = houseRepository.generateUniqueInviteCode(MAX_ATTEMPTS)
            val house = House(
                id = UUID.randomUUID().toString(),
                name = name,
                inviteCode = code,
                creatorId = uid,
                memberIds = listOf(uid),
            )
            houseRepository.upsertHouse(house)
            userRepository.setUserHouse(uid, house.id)
        }
    }

    fun joinHouse() {
        val code = _uiState.value.inviteCodeInput.trim().uppercase()
        if (code.isBlank()) {
            _uiState.update { it.copy(error = "Enter an invite code.") }
            return
        }
        val uid = authRepository.currentUid ?: run {
            _uiState.update { it.copy(error = "Not signed in.") }
            return
        }
        launch {
            requireNoHouse(uid)
            val house = houseRepository.getHouseByInviteCode(code)
                ?: throw IllegalArgumentException("Invalid code")
            if (uid in house.memberIds) {
                throw IllegalStateException("You're already a member of this house.")
            }
            houseRepository.addMember(house.id, uid)
            userRepository.setUserHouse(uid, house.id)
        }
    }

    /** Guard against ending up in two houses: bail if the user already has one. */
    private suspend fun requireNoHouse(uid: String) {
        val existing = userRepository.observeUser(uid).first()?.houseId
        if (existing != null) throw IllegalStateException("You already belong to a house.")
    }

    private fun launch(block: suspend () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val error = runCatching { block() }.exceptionOrNull()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = error?.let { e -> e.message ?: "Something went wrong." },
                )
            }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                HouseSetupViewModel(
                    container().houseRepository,
                    container().authRepository,
                    container().userRepository,
                )
            }
        }
    }
}