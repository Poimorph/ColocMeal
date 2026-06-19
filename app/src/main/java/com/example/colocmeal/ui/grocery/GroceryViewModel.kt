package com.example.colocmeal.ui.grocery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.colocmeal.domain.model.Aisle
import com.example.colocmeal.domain.model.GroceryItem
import com.example.colocmeal.domain.model.Source
import com.example.colocmeal.domain.repository.AuthRepository
import com.example.colocmeal.domain.repository.GroceryRepository
import com.example.colocmeal.domain.repository.UserRepository
import com.example.colocmeal.domain.utils.normalizeName
import com.example.colocmeal.ui.container
import com.example.colocmeal.ui.launchSafe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn

class GroceryViewModel(
    private val houseId: String,
    private val repository: GroceryRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    val items: StateFlow<List<GroceryItem>> =
        repository.observeItems(houseId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Items removed by the last clear/shake, kept so the user can undo. */
    private val _lastCleared = MutableStateFlow<List<GroceryItem>>(emptyList())
    val lastCleared: StateFlow<List<GroceryItem>> = _lastCleared.asStateFlow()

    fun addItem(name: String, aisle: Aisle) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return
        launchSafe {
            repository.addOrMergeItem(
                GroceryItem(
                    id = "",
                    houseId = houseId,
                    name = trimmed,
                    nameNormalized = normalizeName(trimmed),
                    aisle = aisle,
                    isChecked = false,
                    source = Source.MANUAL,
                    addedBy = authRepository.currentUid.orEmpty(),
                )
            )
        }
    }

    fun toggleItemChecked(itemId: String, isChecked: Boolean) {
        val item = items.value.find { it.id == itemId } ?: return
        launchSafe {
            val name = currentUserName()
            repository.setChecked(item, isChecked, name)
        }
    }

    private suspend fun currentUserName(): String {
        val uid = authRepository.currentUid ?: return ""
        return userRepository.observeUser(uid).first()?.displayName.orEmpty()
    }

    fun deleteItem(itemId: String) {
        val item = items.value.find { it.id == itemId } ?: return
        launchSafe { repository.deleteItem(item) }
    }

    /** remove every checked item from the house's list. */
    fun clearBought() {
        val cleared = items.value.filter { it.isChecked }
        if (cleared.isEmpty()) return
        launchSafe {
            _lastCleared.value = cleared
            repository.clearChecked(houseId)
        }
    }

    /** shake on the Shopping tab marks every item bought, then clears them. */
    fun onShakeClear() {
        val current = items.value
        if (current.isEmpty()) return            // empty list -> no-op (error case)
        launchSafe {
            val name = currentUserName()
            // Mark all bought first (drives the check animation), then remove them.
            current.filterNot { it.isChecked }.forEach { repository.setChecked(it, true, name) }
            _lastCleared.value = current
            repository.clearChecked(houseId)
        }
    }

    /** Restore the items removed by the last clear/shake. */
    fun undoClear() {
        val toRestore = _lastCleared.value
        if (toRestore.isEmpty()) return
        launchSafe {
            toRestore.forEach { repository.addOrMergeItem(it.copy(isChecked = false, checkedByName = "")) }
            _lastCleared.value = emptyList()
        }
    }

    fun consumeLastCleared() {
        _lastCleared.value = emptyList()
    }

    companion object {
        fun factory(houseId: String) = viewModelFactory {
            initializer {
                GroceryViewModel(
                    houseId,
                    container().groceryRepository,
                    container().authRepository,
                    container().userRepository,
                )
            }
        }
    }
}
