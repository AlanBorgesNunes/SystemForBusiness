package com.app.systemforbusiness.repository

import com.app.systemforbusiness.models.Usuario
import com.app.systemforbusiness.utils.UiState

interface AuthRepository {
    fun registerUser(email: String, password: String, user: Usuario, result: (UiState<String>) -> Unit)
    fun firebaseLoginGoogle(idToken:String, email:String, result: (UiState<String>) -> Unit)
    fun firebaseAuthGoogle(idToken:String, email:String, nome:String,user:Usuario, result: (UiState<String>) -> Unit)
    fun updateUserInfo(user: Usuario, result: (UiState<String>) -> Unit)
    fun updateUser(hashMap: Map<String,Any>, result: (UiState<String>) -> Unit)
    fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit)
    fun forgotPassword(email: String, result: (UiState<String>) -> Unit)
    fun logout(result: () -> Unit)
    fun storeSession(id: String, result: (Usuario?) -> Unit)
    fun getSession(result: (Usuario?) -> Unit)
}