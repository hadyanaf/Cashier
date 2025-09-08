package com.miredo.cashier.domain.repository

import com.miredo.cashier.domain.model.AuthResult
import com.miredo.cashier.domain.model.AuthState
import com.miredo.cashier.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getAuthState(): Flow<AuthState>
    suspend fun signIn(email: String, password: String): AuthResult
    suspend fun signOut(): Result<Unit>
    suspend fun getCurrentUser(): User?
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
}