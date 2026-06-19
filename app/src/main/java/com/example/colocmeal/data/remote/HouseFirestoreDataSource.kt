package com.example.colocmeal.data.remote

import com.example.colocmeal.data.remote.dto.HouseDto
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class HouseFirestoreDataSource (
    private val firestore: FirebaseFirestore
){
    private val collection = firestore.collection("houses")

    fun observeHouse(houseId: String) : Flow<HouseDto?> =
        callbackFlow {
            val registration = collection.document(houseId).addSnapshotListener {
                snapshot, exception ->
                if (exception!=null) return@addSnapshotListener
                trySend(snapshot?.toObject<HouseDto>()?.copy(id = snapshot.id))

            }
            awaitClose { registration.remove() }

        }

    suspend fun getByInviteCode(inviteCode: String): HouseDto? {
        val doc = collection.whereEqualTo("inviteCode", inviteCode).limit(1)
            .get().await().documents.firstOrNull() ?: return null
        return doc.toObject<HouseDto>()?.copy(id = doc.id)
    }


    suspend fun upsert(house : HouseDto){
        collection.document(house.id).set(house).await()
    }

    suspend fun delete(house: HouseDto){
        collection.document(house.id).delete().await()
    }

    suspend fun addMember(houseId: String, userId: String){
        collection.document(houseId).update("memberIds", FieldValue.arrayUnion(userId)).await()
    }

    suspend fun removeMember(houseId: String, userId: String){
        collection.document(houseId).update("memberIds", FieldValue.arrayRemove(userId)).await()
    }

}
