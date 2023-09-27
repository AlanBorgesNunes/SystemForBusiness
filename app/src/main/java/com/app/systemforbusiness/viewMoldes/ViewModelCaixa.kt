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

    private val _getSaidas = MutableLiveData<UiState<ArrayList<MyCaixa>>>()
    val getSaidas: LiveData<UiState<ArrayList<MyCaixa>>>
        get() = _getSaidas

    private val _getNumerosEntradas = MutableLiveData<UiState<Any>>()
    val getNumerosEntradas: LiveData<UiState<Any>>
        get() = _getNumerosEntradas

    private val _getNumerosSaidas = MutableLiveData<UiState<Any>>()
    val getNumerosSaidas: LiveData<UiState<Any>>
        get() = _getNumerosSaidas

    fun getEntradas(mes:String){
        _getEntradas.value = UiState.Loading
        repository.getEntradas(mes){
            Log.d("TAG", "getEntradasViewlModel: $it ")
            _getEntradas.value = it
        }
    }

    fun getNumerosEntradas(mes:String){
        _getNumerosEntradas.value = UiState.Loading
        repository.getNumerosEntradas(mes) {
            _getNumerosEntradas.value = it
        }
    }

    fun getSaidas(mes: String){
        _getSaidas.value = UiState.Loading
        repository.getSaidas(mes){
            _getSaidas.value = it
        }
    }

    fun getNumerosSaidas(mes: String){
        _getNumerosSaidas.value = UiState.Loading
        repository.getNumerosSaidas(mes){
            _getNumerosSaidas.value = it
        }
    }
}