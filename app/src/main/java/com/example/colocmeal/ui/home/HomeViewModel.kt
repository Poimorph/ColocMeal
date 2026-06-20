package com.example.colocmeal.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.colocmeal.domain.model.House
import com.example.colocmeal.domain.repository.AuthRepository
import com.example.colocmeal.domain.repository.HouseRepository
import com.example.colocmeal.domain.repository.UserRepository
import com.example.colocmeal.ui.container
import com.example.colocmeal.ui.house.HouseMember
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val houseId: String,
    private val houseRepository: HouseRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    val house: StateFlow<House?> =
        houseRepository.observeHouse(houseId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val syncedMembers = mutableSetOf<String>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val members: StateFlow<List<HouseMember>> =
        house.flatMapLatest { house ->
            val ids = house?.memberIds.orEmpty()
            // Pull each member's user doc into the local DB so we can show names.
            ids.forEach { id -> if (syncedMembers.add(id)) userRepository.startSync(id) }
            if (ids.isEmpty()) {
                flowOf(emptyList())
            } else {
                combine(
                    ids.map { id ->
                        userRepository.observeUser(id).map { user ->
                            HouseMember(id, user?.displayName?.takeIf { it.isNotBlank() } ?: id)
                        }
                    }
                ) { it.toList() }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun quitHouse() {
        val uid = authRepository.currentUid ?: return
        viewModelScope.launch {
            houseRepository.removeMember(houseId, uid)
            userRepository.setUserHouse(uid, null)
        }
    }

    fun disconnect() {
        authRepository.signOut()
    }

    companion object {
        fun factory(houseId: String) = viewModelFactory {
            initializer {
                HomeViewModel(
                    houseId,
                    container().houseRepository,
                    container().userRepository,
                    container().authRepository,
                )
            }
        }
    }
}
