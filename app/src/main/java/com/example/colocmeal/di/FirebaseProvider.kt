package com.example.colocmeal.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Manual provider for the Firebase singletons (no DI framework yet).
 *
 * Pass [FirebaseProvider.firestore] into each `XxxFirestoreDataSource`, and use
 * [FirebaseProvider.auth] wherever the current `uid` is needed.
 */
object FirebaseProvider {
    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
}