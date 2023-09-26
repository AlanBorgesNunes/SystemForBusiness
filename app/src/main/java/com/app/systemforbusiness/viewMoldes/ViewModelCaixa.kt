package com.app.systemforbusiness.viewMoldes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.systemforbusiness.models.MyCaixa
import com.app.systemforbusiness.repository.CaixaRepository
import com.app.systemforbusiness.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModelCaixa @Inject constructor(
    val repository: CaixaRepository
): ViewModel() {

    private val _getEntradas = MutableLiveData<UiState<ArrayList<MyCaixa>>>()
    val getEntradas: LiveData<UiState<ArrayList<MyCaixa>>>
        get() = _getEntradas


    private val _getNumerosEntradas = MutableLiveData<UiState<ArrayList<Int>>>()
    val getNumerosEntradas: LiveData<UiState<ArrayList<Int>>>
        get() = _getNumerosEntradas

    fun getEntradas(){
        _getEntradas.value = UiState.Loading
        repository.getEntradas(){
            Log.d("TAG", "getEntradasViewlModel: $it ")
            _getEntradas.value = it
        }
    }

    fun getNumerosEntradas(){
        _getNumerosEntradas.value = UiState.Loading
        repository.getNumerosEntradas {
            _getNumerosEntradas.value = it
        }
    }
}