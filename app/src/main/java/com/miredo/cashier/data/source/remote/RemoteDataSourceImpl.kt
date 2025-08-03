package com.miredo.cashier.data.source.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.miredo.cashier.data.model.AttendanceTask
import com.miredo.cashier.data.model.Sale
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : RemoteDataSource {
    override fun getReports(): Flow<List<AttendanceTask>> = callbackFlow {
        val listener = firestore.collection("reports")
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

        awaitClose { listener.remove() }
    }

    override fun getSales(reportId: String): Flow<List<Sale>> = callbackFlow {
        val listener = firestore.collection("reports")
            .document(reportId)
            .collection("sales")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val sales = snapshot?.toObjects(Sale::class.java).orEmpty()
                trySend(sales)
            }

        awaitClose { listener.remove() }
    }

    override suspend fun saveCheckInData(id: String, data: AttendanceTask) {
        firestore.collection("reports")
            .document(id)
            .set(data)
            .await()
    }

    override suspend fun addSale(reportId: String, sale: Sale) {
        firestore.collection("reports")
            .document(reportId)
            .collection("sales")
            .add(sale) // Auto-generates a unique ID
            .await()
    }
}