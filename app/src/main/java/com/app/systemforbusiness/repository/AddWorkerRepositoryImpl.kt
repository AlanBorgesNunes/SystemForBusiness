package com.app.systemforbusiness.repository

import com.app.systemforbusiness.models.Workers
import com.app.systemforbusiness.utils.UiState
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class AddWorkerRepositoryImpl(
    val database: FirebaseFirestore
): AddWorkerRepository {
    override fun addWorker(workers: Workers, result: (UiState<String>) -> Unit) {
        val document = database.collection("Workers").document(workers.name.toString())
        document.set(workers)
            .addOnSuccessListener {
                result.invoke(UiState.Success(
                    "Resgistered worker"
                ))
            }.addOnFailureListener {
                result.invoke(UiState.Failure(
                    it.localizedMessage
                ))
            }
    }

    override fun getWorkers(workers: Workers, result: (UiState<ArrayList<Workers>>) -> Unit) {
        val document = database.collection("Workers")
        document.addSnapshotListener { value, error ->
            if (value!=null){
               val list = arrayListOf<Workers>()
               val workers = value.toObjects(Workers::class.java)
                if (workers != null){
                    list.addAll(workers)
                    result.invoke(UiState.Success(
                        list
                    ))
                }else{
                    result.invoke(UiState.Failure(
                        "Result null"
                    ))
                }

            }else{
                result.invoke(UiState.Failure(
                    error?.localizedMessage
                ))
            }
        }
    }
}