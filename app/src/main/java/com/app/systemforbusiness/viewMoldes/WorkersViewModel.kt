package com.app.systemforbusiness.viewMoldes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.systemforbusiness.models.Cliente
import com.app.systemforbusiness.models.Workers
import com.app.systemforbusiness.repository.AddWorkerRepository
import com.app.systemforbusiness.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class WorkersViewModel @Inject constructor(
    val repository: AddWorkerRepository
) : ViewModel(){

    private val _addWorker = MutableLiveData<UiState<String>>()
    val addWorker: LiveData<UiState<String>>
        get() = _addWorker

    private val _getWorkers = MutableLiveData<UiState<ArrayList<Workers>>>()
    val getWorkers: LiveData<UiState<ArrayList<Workers>>>
        get() = _getWorkers


    fun getWorkers(workers: Workers){
        _getWorkers.value = UiState.Loading
        repository.getWorkers(workers){
            _getWorkers.value = it
        }
    }

    fun addWorkers(workers: Workers){
        _getWorkers.value = UiState.Loading
        repository.addWorker(workers){
            _addWorker.value = it
        }
    }

}