package com.example.colocmeal.data.remote

import com.example.colocmeal.data.remote.dto.RecipeDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RecipeFirestoreDataSource(
    private val firestore: FirebaseFirestore
) {
    private val collection = firestore.collection("recipes")

    // Shared recipes for a house
    fun observeSharedRecipes(houseId: String): Flow<List<RecipeDto>> =
        callbackFlow {
            val registration = collection
                .whereEqualTo("houseId", houseId)
                .whereEqualTo("isShared", "true")
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) return@addSnapshotListener
                    val recipes = snapshot?.documents?.mapNotNull { doc ->
                        doc.toObject<RecipeDto>()?.copy(id = doc.id)
                    }.orEmpty()
                    trySend(recipes)
                }
            awaitClose { registration.remove() }
        }

    // Private recipes authored by a user
    fun observePrivateRecipes(authorId: String): Flow<List<RecipeDto>> =
        callbackFlow {
            val registration = collection
                .whereEqualTo("authorId", authorId)
                .whereEqualTo("isShared", "false")
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) return@addSnapshotListener
                    val recipes = snapshot?.documents?.mapNotNull { doc ->
                        doc.toObject<RecipeDto>()?.copy(id = doc.id)
                    }.orEmpty()
                    trySend(recipes)
                }
            awaitClose { registration.remove() }
        }

    suspend fun upsert(recipe: RecipeDto) {
        collection.document(recipe.id).set(recipe).await()
    }

    suspend fun delete(recipeId: String) {
        collection.document(recipeId).delete().await()
    }
}
