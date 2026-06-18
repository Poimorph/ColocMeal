package com.example.colocmeal.ui.house

data class HouseSetupUiState(
    val houseName: String = "",
    val inviteCodeInput: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class HouseSetupViewModel {

}