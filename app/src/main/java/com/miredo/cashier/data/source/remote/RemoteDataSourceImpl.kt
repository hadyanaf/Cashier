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

    override suspend fun getSale(reportId: String, saleId: String): Sale? {
        return try {
            firestore.collection("reports")
                .document(reportId)
                .collection("sales")
                .document(saleId)
                .get()
                .await()
                .toObject(Sale::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun addSale(reportId: String, sale: Sale) {
        val collectionRef = firestore.collection("reports")
            .document(reportId)
            .collection("sales")

        if (sale.id.isNullOrEmpty()) {
            // Auto-generate ID and insert new sale
            val newDocRef = collectionRef.document() // generate ID
            val saleWithId = sale.copy(id = newDocRef.id)
            newDocRef.set(saleWithId).await()
        } else {
            // Update existing sale
            collectionRef.document(sale.id)
                .set(sale)
                .await()
        }
    }

    override suspend fun updateSale(reportId: String, saleId: String, sale: Sale) {
        firestore.collection("reports")
            .document(reportId)
            .collection("sales")
            .document(saleId)
            .set(sale)
            .await()
    }

    override suspend fun deleteSale(reportId: String, saleId: String) {
        firestore.collection("reports")
            .document(reportId)
            .collection("sales")
            .document(saleId)
            .delete()
            .await()
    }
}