package com.example.colocmeal

import android.app.Application
import com.example.colocmeal.di.AppContainer

class ColocMealApplication: Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}