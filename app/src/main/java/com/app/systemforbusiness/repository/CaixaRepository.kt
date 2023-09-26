package com.app.systemforbusiness.repository

import com.app.systemforbusiness.models.MyCaixa
import com.app.systemforbusiness.utils.UiState

interface CaixaRepository {
    fun getEntradas( result: (UiState<ArrayList<MyCaixa>>) -> Unit)

    fun getNumerosEntradas(result: (UiState<ArrayList<Int>>) -> Unit)
}