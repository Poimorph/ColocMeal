package com.example.colocmeal.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.colocmeal.domain.model.House
import com.example.colocmeal.domain.repository.HouseRepository
import com.example.colocmeal.ui.container
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    houseId: String,
    houseRepository: HouseRepository,
) : ViewModel() {

    val house: StateFlow<House?> =
        houseRepository.observeHouse(houseId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    companion object {
        fun factory(houseId: String) = viewModelFactory {
            initializer { HomeViewModel(houseId, container().houseRepository) }
        }
    }
}