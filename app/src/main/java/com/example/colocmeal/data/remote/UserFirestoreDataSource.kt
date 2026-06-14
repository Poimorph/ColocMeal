package com.example.colocmeal.data.remote

import com.example.colocmeal.data.remote.dto.UserDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserFirestoreDataSource(
    private val firestore: FirebaseFirestore
) {
    private val collection = firestore.collection("users")

    // SINGLE document listener — /users/{uid}
    fun observeUser(uid: String): Flow<UserDto?> =
        callbackFlow {
            val registration = collection.document(uid).addSnapshotListener {
                    snapshot, exception ->
                if (exception != null) return@addSnapshotListener
                trySend(snapshot?.toObject<UserDto>()?.copy(uid = snapshot.id))
            }
            awaitClose { registration.remove() }
        }

    suspend fun upsert(user: UserDto) {
        collection.document(user.uid).set(user).await()
    }

    suspend fun setHouseId(uid: String, houseId: String?) {
        collection.document(uid).update("houseId", houseId ?: "").await()
    }
}
