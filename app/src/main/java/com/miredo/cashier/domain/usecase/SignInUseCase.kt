package com.miredo.cashier.domain.usecase

import com.miredo.cashier.domain.model.AuthResult
import com.miredo.cashier.domain.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): AuthResult {
        return authRepository.signIn(email, password)
    }
}