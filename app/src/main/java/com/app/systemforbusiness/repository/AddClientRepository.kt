package com.app.systemforbusiness.repository

import com.app.systemforbusiness.models.Cliente
import com.app.systemforbusiness.models.UltimosProcedimentos
import com.app.systemforbusiness.models.Usuario
import com.app.systemforbusiness.utils.UiState

interface AddClientRepository {
    fun addCliet(cliente: Cliente, result: (UiState<String>) -> Unit)
    fun updateClient(hashMap: Map<String,Any>,cliente: Cliente, result: (UiState<String>) -> Unit)
    fun getClient(cliente: Cliente, result: (UiState<ArrayList<Cliente>>) -> Unit)
    fun getUltimosProcedimentos(idClient: String, result: (UiState<ArrayList<UltimosProcedimentos>>) -> Unit)
}