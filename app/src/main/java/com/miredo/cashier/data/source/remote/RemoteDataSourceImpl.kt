package com.miredo.cashier.data.source.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.miredo.cashier.data.model.AttendanceTask
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : RemoteDataSource {
    override fun getReports(): Flow<List<AttendanceTask>> = callbackFlow {
        val collection = firestore.collection("reports")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val reports = snapshot?.toObjects(AttendanceTask::class.java)
                if (reports != null) {
                    trySend(reports)
                }

            }

        awaitClose { collection.remove() }
    }

    override suspend fun saveCheckInData(id: String, data: AttendanceTask) {
        val collection = firestore.collection("reports")
            .document(id)
            .set(data)
            .await()
    }
}