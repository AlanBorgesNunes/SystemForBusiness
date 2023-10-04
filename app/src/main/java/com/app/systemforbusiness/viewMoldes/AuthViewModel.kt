package com.app.systemforbusiness.viewMoldes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.systemforbusiness.models.Usuario
import com.app.systemforbusiness.repository.AuthRepository
import com.app.systemforbusiness.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val repository: AuthRepository
) : ViewModel() {
    private val _register = MutableLiveData<UiState<String>>()
    val register: LiveData<UiState<String>>
        get() = _register

    private val _signUpGoogle = MutableLiveData<UiState<String>>()
    val signUpGoogle: LiveData<UiState<String>>
        get() = _signUpGoogle

    private val _loginGoogle = MutableLiveData<UiState<String>>()
    val loginGoogle: LiveData<UiState<String>>
        get() = _loginGoogle

    private val _login = MutableLiveData<UiState<String>>()
    val login: LiveData<UiState<String>>
        get() = _login

    private val _forgotPassword = MutableLiveData<UiState<String>>()
    val forgotPassword: LiveData<UiState<String>>
        get() = _forgotPassword

    private val _updateUser = MutableLiveData<UiState<String>>()
    val updateUser: LiveData<UiState<String>>
        get() = _updateUser

    fun register(
        email: String,
        password: String,
        user: Usuario
    ) {
        viewModelScope.launch {
            _register.value = UiState.Loading
            repository.registerUser(
                email = email,
                password = password,
                user = user
            ) { _register.value = it }
        }
    }

    fun login(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _login.value = UiState.Loading
            repository.loginUser(
                email,
                password
            ) {
                _login.value = it
            }
        }

    }
}