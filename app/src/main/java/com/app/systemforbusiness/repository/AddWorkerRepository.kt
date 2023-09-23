package com.app.systemforbusiness.repository

import com.app.systemforbusiness.models.Workers
import com.app.systemforbusiness.utils.UiState

interface AddWorkerRepository {

    fun addWorker(workers: Workers, result: (UiState<String>)-> Unit)
    fun getWorkers(workers: Workers, result: (UiState<ArrayList<Workers>>) -> Unit)
}