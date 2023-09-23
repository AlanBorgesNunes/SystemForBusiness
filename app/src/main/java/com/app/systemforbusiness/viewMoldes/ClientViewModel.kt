package com.app.systemforbusiness.viewMoldes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.systemforbusiness.models.Cliente
import com.app.systemforbusiness.models.UltimosProcedimentos
import com.app.systemforbusiness.repository.AddClientRepository
import com.app.systemforbusiness.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ClientViewModel @Inject constructor(
    val respository: AddClientRepository
) : ViewModel(){

    private val _updateClient = MutableLiveData<UiState<String>>()
    val updateClient: LiveData<UiState<String>>
        get() = _updateClient

    private val _getClient = MutableLiveData<UiState<ArrayList<Cliente>>>()
    val getClient: LiveData<UiState<ArrayList<Cliente>>>
        get() = _getClient

    private val _getUltimosProcedimentos = MutableLiveData<UiState<ArrayList<UltimosProcedimentos>>>()
    val getUltimosProcedimentos: LiveData<UiState<ArrayList<UltimosProcedimentos>>>
        get() = _getUltimosProcedimentos

    private val _addClient = MutableLiveData<UiState<String>>()
    val addClient: LiveData<UiState<String>>
        get() = _addClient

    fun updateClient(
        hashMap: Map<String,Any>,
        cliente: Cliente
    ){
        _updateClient.value = UiState.Loading
        respository.updateClient(hashMap,cliente){
            _updateClient.value = it
        }
    }

    fun addClient(
        cliente: Cliente
    ){
        _addClient.value = UiState.Loading
        respository.addCliet(
            cliente
        ){
            _addClient.value = it
        }
    }

    fun getClient(
        cliente: Cliente
    ){
      _getClient.value = UiState.Loading
      respository.getClient(
          cliente
      ){
        _getClient.value = it
      }
    }

    fun getUltimosProcedimentos(idClient: String){
        _getUltimosProcedimentos.value = UiState.Loading
        respository.getUltimosProcedimentos(
            idClient
        ){
            _getUltimosProcedimentos.value = it
        }
    }
}