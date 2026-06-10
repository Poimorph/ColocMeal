package com.example.colocmeal.data.remote

import com.example.colocmeal.data.remote.dto.GroceryItemDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class GroceryFirestoreDataSource(
    private val firestore: FirebaseFirestore
) {
    private val collection = firestore.collection("groceryItems")

    fun observerItems(houseId:String): Flow<List<GroceryItemDto>> =
        callbackFlow {
            val registration = collection
                .whereEqualTo("houseId", houseId)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) return@addSnapshotListener
                    val items = snapshot?.documents?.mapNotNull { doc ->
                        doc.toObject(GroceryItemDto::class.java)?.copy(id = doc.id)

                    }.orEmpty()
                    trySend(items)
                }
            awaitClose { registration.remove() }
        }
    suspend fun upsert(item: GroceryItemDto){
        collection.document(item.id).set(item).await()
    }

    suspend fun  delete(itemId: String) {
        collection.document(itemId).delete().await()
    }
}
