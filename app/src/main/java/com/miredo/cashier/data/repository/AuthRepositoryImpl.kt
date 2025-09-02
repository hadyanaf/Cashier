package com.miredo.cashier.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.miredo.cashier.domain.model.AuthResult
import com.miredo.cashier.domain.model.AuthState
import com.miredo.cashier.domain.model.User
import com.miredo.cashier.domain.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override fun getAuthState(): Flow<AuthState> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val firebaseUser = auth.currentUser
            val authState = if (firebaseUser != null) {
                AuthState.Authenticated(firebaseUser.toUser())
            } else {
                AuthState.Unauthenticated
            }
            trySend(authState)
        }
        
        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }

    override suspend fun signIn(email: String, password: String): AuthResult {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user?.toUser()
            if (user != null) {
                AuthResult(isSuccess = true, user = user)
            } else {
                AuthResult(isSuccess = false, errorMessage = "Failed to get user information")
            }
        } catch (e: Exception) {
            AuthResult(isSuccess = false, errorMessage = e.message ?: "Sign in failed")
        }
    }

    override suspend fun signUp(email: String, password: String): AuthResult {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user?.toUser()
            if (user != null) {
                AuthResult(isSuccess = true, user = user)
            } else {
                AuthResult(isSuccess = false, errorMessage = "Failed to create user")
            }
        } catch (e: Exception) {
            AuthResult(isSuccess = false, errorMessage = e.message ?: "Sign up failed")
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): User? {
        return firebaseAuth.currentUser?.toUser()
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun FirebaseUser.toUser(): User {
        return User(
            uid = uid,
            email = email ?: "",
            displayName = displayName
        )
    }
}