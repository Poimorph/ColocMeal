package com.example.colocmeal.data.remote

import com.example.colocmeal.data.remote.dto.IngredientDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class IngredientFirestoreDataSource(
    private val firestore: FirebaseFirestore
) {
    private val collection = firestore.collection("ingredients")

    fun observeIngredients(houseId: String): Flow<List<IngredientDto>> =
        callbackFlow {
            val registration = collection
                .whereEqualTo("houseId", houseId)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) return@addSnapshotListener
                    val items = snapshot?.documents?.mapNotNull { doc ->
                        doc.toObject(IngredientDto::class.java)?.copy(id = doc.id)
                    }.orEmpty()
                    trySend(items)
                }
            awaitClose { registration.remove() }
        }

    suspend fun upsert(ingredient: IngredientDto) {
        collection.document(ingredient.id).set(ingredient).await()
    }
}