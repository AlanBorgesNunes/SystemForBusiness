package com.app.systemforbusiness.repository

import com.app.systemforbusiness.models.Cliente
import com.app.systemforbusiness.models.UltimosProcedimentos
import com.app.systemforbusiness.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddClientRepositoryImp(
    val auth: FirebaseAuth,
    val database: FirebaseFirestore
): AddClientRepository {
    override fun addCliet(cliente: Cliente, result: (UiState<String>) -> Unit) {
        cliente.idClient = System.currentTimeMillis().toString()
        val document = database.collection("clientes").document(cliente.idClient!!)
        document.set(cliente)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("registered customer!")
                )
            }.addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }

    }

    override fun updateClient(hashMap: Map<String, Any>, cliente: Cliente, result: (UiState<String>) -> Unit) {
        val document = database.collection("clientes").document(cliente.name!!)
        document
            .update(hashMap)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success(
                        it.toString()
                    )
                )
            }.addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    override fun getClient(cliente: Cliente, result: (UiState<ArrayList<Cliente>>) -> Unit) {
        val document = database.collection("clientes")
        document.addSnapshotListener { value, error ->
            if (value != null){
                val list = arrayListOf<Cliente>()
                val clientes = value.toObjects(Cliente::class.java)
                list.addAll(clientes)
                result.invoke(UiState.Success(list))
            }else{
                result.invoke(
                    UiState.Failure(
                        error?.localizedMessage
                    )
                )
            }
        }

    }

    override fun getUltimosProcedimentos(idClient: String, result: (UiState<ArrayList<UltimosProcedimentos>>) -> Unit) {
        database.collection("clientes")
            .document(idClient)
            .collection("UltimosProcedimentos")
            .addSnapshotListener { value, error ->
                if (value != null){
                    val list = arrayListOf<UltimosProcedimentos>()
                    val doc = value.toObjects(UltimosProcedimentos::class.java)
                    list.addAll(doc)
                    result.invoke(UiState.Success(list))
                }else{
                    result.invoke(
                        UiState.Failure(
                            error?.localizedMessage
                        )
                    )
                }
            }
    }
}