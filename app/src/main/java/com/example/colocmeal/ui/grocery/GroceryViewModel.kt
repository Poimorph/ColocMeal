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
import com.example.colocmeal.ui.container
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.Normalizer

class GroceryViewModel(
    private val houseId: String,
    private val repository: GroceryRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    val items: StateFlow<List<GroceryItem>> =
        repository.observeItems(houseId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addItem(name: String) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return
        viewModelScope.launch {
            repository.addOrMergeItem(
                GroceryItem(
                    id = "",
                    houseId = houseId,
                    name = trimmed,
                    nameNormalized = normalize(trimmed),
                    aisle = Aisle.OTHER,
                    isChecked = false,
                    source = Source.MANUAL,
                    addedBy = authRepository.currentUid.orEmpty(),
                )
            )
        }
    }

    fun toggleItemChecked(itemId: String, isChecked: Boolean) {
        val item = items.value.find { it.id == itemId } ?: return
        viewModelScope.launch { repository.setChecked(item, isChecked) }
    }

    fun deleteItem(itemId: String) {
        val item = items.value.find { it.id == itemId } ?: return
        viewModelScope.launch { repository.deleteItem(item) }
    }

    private fun normalize(value: String): String =
        Normalizer.normalize(value.lowercase(), Normalizer.Form.NFD)
            .replace("\\p{Mn}+".toRegex(), "")

    companion object {
        fun factory(houseId: String) = viewModelFactory {
            initializer {
                GroceryViewModel(houseId, container().groceryRepository, container().authRepository)
            }
        }
    }
}
