package com.app.systemforbusiness.repository

import com.app.systemforbusiness.models.MyCaixa
import com.app.systemforbusiness.utils.UiState

interface CaixaRepository {
    fun getEntradas(mes: String, result: (UiState<ArrayList<MyCaixa>>) -> Unit)

    fun getNumerosEntradas(mes: String,result: (UiState<Any>) -> Unit)

    fun getSaidas(mes: String, result: (UiState<ArrayList<MyCaixa>>) -> Unit)

    fun getNumerosSaidas(mes: String,result: (UiState<Any>) -> Unit)
}