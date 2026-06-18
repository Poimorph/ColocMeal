package com.example.colocmeal.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.colocmeal.domain.repository.AuthRepository
import com.example.colocmeal.domain.repository.UserRepository
import com.example.colocmeal.ui.container
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed interface  RootState{
    data object Loading : RootState
    data object SignedOut : RootState
    data class NeedsHouse(val uid: String): RootState
    data class Ready(val uid: String, val houseId:String): RootState

}

class RootViewModel(
    authRepository: AuthRepository,
    userRepository: UserRepository
) : ViewModel(){

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<RootState> =
        authRepository.authState()
            .flatMapLatest { uid ->
                if (uid == null) flowOf(RootState.SignedOut)
                else userRepository.observeUser(uid).map { user ->
                    when {
                        user?.houseId != null -> RootState.Ready(uid, user.houseId)
                        else                   -> RootState.NeedsHouse(uid)
                    }
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), RootState.Loading)
    companion object {
        val Factory = viewModelFactory { initializer { RootViewModel(container().authRepository, container().userRepository) } }
        }
}