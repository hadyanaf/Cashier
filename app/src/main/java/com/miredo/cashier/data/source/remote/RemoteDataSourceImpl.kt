package com.miredo.cashier.data.source.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.miredo.cashier.data.model.AttendanceTask
import com.miredo.cashier.data.model.Sale
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : RemoteDataSource {

    private fun getCurrentUserEmail(): String {
        return auth.currentUser?.email ?: throw IllegalStateException("User not authenticated or email not available")
    }

    private fun getCurrentUserId(): String {
        return auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")
    }

    private fun getUserReportsCollection() = firestore
        .collection("users")
        .document(getCurrentUserEmail())
        .collection("reports")

    override fun getReports(): Flow<List<AttendanceTask>> = callbackFlow {
        val listener = getUserReportsCollection()
            .orderBy("date")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val reports = snapshot?.toObjects(AttendanceTask::class.java).orEmpty()
                trySend(reports)
            }

        awaitClose { listener.remove() }
    }

    override fun getSales(reportId: String): Flow<List<Sale>> = callbackFlow {
        val listener = getUserReportsCollection()
            .document(reportId)
            .collection("sales")
            .orderBy("createdAt")
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

    override suspend fun createReport(date: String): String {
        val userId = getCurrentUserId()
        val userEmail = getCurrentUserEmail()

        val attendanceTask = AttendanceTask.create(
            userId = userId,
            date = date
        ).copy(id = date) // Use date as the report ID

        getUserReportsCollection()
            .document(date) // Use date as document ID
            .set(attendanceTask)
            .await()

        return date // Return date as the report ID
    }

    override suspend fun saveCheckInData(id: String, data: AttendanceTask) {
        val userId = getCurrentUserId()
        val reportId = id.ifEmpty {
            data.date // Use date as report ID if id is empty
        }

        val dataWithUser = data.copy(
            id = reportId,
            userId = userId
        )

        getUserReportsCollection()
            .document(reportId)
            .set(dataWithUser)
            .await()
    }

    override suspend fun getSale(reportId: String, saleId: String): Sale? {
        return try {
            getUserReportsCollection()
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
        val collectionRef = getUserReportsCollection()
            .document(reportId)
            .collection("sales")

        if (sale.id.isNullOrEmpty()) {
            // Generate timestamp-based ID (HH:mm:ss)
            val currentTime = LocalTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
            val timestampId = currentTime.format(formatter)
            
            val saleWithId = sale.copy(id = timestampId)
            collectionRef.document(timestampId).set(saleWithId).await()
        } else {
            // Update existing sale
            collectionRef.document(sale.id)
                .set(sale)
                .await()
        }
    }

    override suspend fun updateSale(reportId: String, saleId: String, sale: Sale) {
        getUserReportsCollection()
            .document(reportId)
            .collection("sales")
            .document(saleId)
            .set(sale)
            .await()
    }

    override suspend fun deleteSale(reportId: String, saleId: String) {
        getUserReportsCollection()
            .document(reportId)
            .collection("sales")
            .document(saleId)
            .delete()
            .await()
    }
}