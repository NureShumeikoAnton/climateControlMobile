package com.example.climatecontrolmobile.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.climatecontrolmobile.data.repository.AuthRepository

class AuthViewModel(context: Context) : ViewModel() {
    private val authRepository = AuthRepository(context)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    init {
        checkAuthenticationStatus()
    }

    private fun checkAuthenticationStatus() {
        _isAuthenticated.value = authRepository.isAuthenticated()
    }

    fun authenticate(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _authError.value = null
            try {
                val token = authRepository.login(email, password)
                authRepository.saveToken(token)

                authRepository.initializeFcmAfterLogin()
                
                _isAuthenticated.value = true
            } catch (e: Exception) {
                _authError.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun logout() {
        authRepository.logout()
        _isAuthenticated.value = false
        _authError.value = null
    }
}