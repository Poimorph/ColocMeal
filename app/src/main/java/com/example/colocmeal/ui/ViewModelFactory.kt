package com.example.colocmeal.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.colocmeal.ColocMealApplication
import com.example.colocmeal.di.AppContainer

fun CreationExtras.container(): AppContainer =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
            as ColocMealApplication).container