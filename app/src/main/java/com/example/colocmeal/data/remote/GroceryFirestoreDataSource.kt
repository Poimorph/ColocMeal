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

    fun observeItems(houseId:String): Flow<List<GroceryItemDto>> =
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

    suspend fun setChecked(itemId: String, checked: Boolean, checkedByName: String) {
        collection.document(itemId)
            .update(mapOf("isChecked" to checked, "checkedByName" to checkedByName))
            .await()
    }

    suspend fun  delete(itemId: String) {
        collection.document(itemId).delete().await()
    }

    /** Batch-delete every checked item of a house so it does not resync back. */
    suspend fun deleteChecked(houseId: String) {
        val snapshot = collection
            .whereEqualTo("houseId", houseId)
            .whereEqualTo("isChecked", true)
            .get()
            .await()
        if (snapshot.isEmpty) return
        val batch = firestore.batch()
        snapshot.documents.forEach { batch.delete(it.reference) }
        batch.commit().await()
    }
}
