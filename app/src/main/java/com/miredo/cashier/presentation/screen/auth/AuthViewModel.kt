package com.miredo.cashier.presentation.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miredo.cashier.domain.model.AuthResult
import com.miredo.cashier.domain.model.AuthState
import com.miredo.cashier.domain.usecase.GetAuthStateUseCase
import com.miredo.cashier.domain.usecase.SendPasswordResetEmailUseCase
import com.miredo.cashier.domain.usecase.SignInUseCase
import com.miredo.cashier.domain.usecase.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getAuthStateUseCase: GetAuthStateUseCase,
    private val signInUseCase: SignInUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val sendPasswordResetEmailUseCase: SendPasswordResetEmailUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            getAuthStateUseCase().collectLatest { state ->
                _authState.value = state
            }
        }
    }

    fun signIn(email: String, password: String) {
        if (!isValidEmail(email)) {
            _errorMessage.value = "Masukkan alamat email yang valid"
            return
        }
        
        if (password.isBlank() || password.length < 6) {
            _errorMessage.value = "Kata sandi harus minimal 6 karakter"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            val result = signInUseCase(email, password)
            
            _isLoading.value = false
            
            if (!result.isSuccess) {
                _errorMessage.value = result.errorMessage
            }
        }
    }


    fun signOut() {
        viewModelScope.launch {
            _isLoading.value = true
            signOutUseCase()
            _isLoading.value = false
        }
    }

    fun sendPasswordResetEmail(email: String) {
        if (!isValidEmail(email)) {
            _errorMessage.value = "Masukkan alamat email yang valid"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            val result = sendPasswordResetEmailUseCase(email)
            
            _isLoading.value = false
            
            if (result.isSuccess) {
                _errorMessage.value = "Email reset kata sandi berhasil dikirim"
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Gagal mengirim email reset kata sandi"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}