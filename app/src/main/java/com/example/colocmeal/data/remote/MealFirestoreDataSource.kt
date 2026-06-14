package com.example.colocmeal.data.remote

import com.example.colocmeal.data.remote.dto.MealPlanDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class MealFirestoreDataSource(
    private val firestore: FirebaseFirestore
) {
    private val collection = firestore.collection("mealPlans")

    fun observeWeek(houseId: String, weekStart: String): Flow<List<MealPlanDto>> =
        callbackFlow {
            val registration = collection
                .whereEqualTo("houseId", houseId)
                .whereEqualTo("weekStart", weekStart)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) return@addSnapshotListener
                    val meals = snapshot?.documents?.mapNotNull { doc ->
                        doc.toObject<MealPlanDto>()?.copy(id = doc.id)
                    }.orEmpty()
                    trySend(meals)
                }
            awaitClose { registration.remove() }
        }

    suspend fun upsert(mealPlan: MealPlanDto) {
        collection.document(mealPlan.id).set(mealPlan).await()
    }

    suspend fun delete(mealPlanId: String) {
        collection.document(mealPlanId).delete().await()
    }
}