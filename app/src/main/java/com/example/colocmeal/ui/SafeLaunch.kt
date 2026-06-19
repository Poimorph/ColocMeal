package com.example.colocmeal.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private val viewModelErrorHandler = CoroutineExceptionHandler { _, throwable ->
    // A failed write (e.g. Firestore PERMISSION_DENIED, offline) must not crash the app.
    // CancellationException is never delivered here, so structured concurrency is preserved.
    Log.e("ColocMeal", "ViewModel coroutine failed: ${throwable.message}", throwable)
}

/**
 * Like [viewModelScope].launch, but a thrown exception is logged instead of crashing the app.
 * Use for repository writes that hit the network.
 */
fun ViewModel.launchSafe(block: suspend CoroutineScope.() -> Unit): Job =
    viewModelScope.launch(viewModelErrorHandler, block = block)