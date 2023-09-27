package com.app.systemforbusiness.repository

import android.util.Log
import com.app.systemforbusiness.models.MyCaixa
import com.app.systemforbusiness.utils.UiState
import com.google.firebase.firestore.FirebaseFirestore

class CaixaRepositoryImp(
    val database: FirebaseFirestore,
) : CaixaRepository {


    override fun getEntradas(mes: String, result: (UiState<ArrayList<MyCaixa>>) -> Unit) {
        val document = database.collection("Caixa")
            .document("Entrada")
            .collection(mes)
        document.addSnapshotListener { value, error ->
            if (value != null){
                val list = arrayListOf<MyCaixa>()
                val box  = value.toObjects(MyCaixa::class.java)
                list.addAll(box)
                Log.d("TAG", "getEntradasRepository: $box ")
                result.invoke(UiState.Success(
                    list
                ))
            }else{
                result.invoke(UiState.Failure(
                    error?.localizedMessage
                ))
            }
        }
    }

    override fun getNumerosEntradas(mes: String, result: (UiState<Any>) -> Unit) {
        val document = database.collection("Caixa")
            .document("Entrada")
            .collection(mes)
        document
            .get()
            .addOnSuccessListener {
                var soma = 0

                if (it.isEmpty) {
                    UiState.Failure(
                        "Campo vazio!"
                    )
                } else{
                    for (doc in it) {
                        val item = doc.toObject(MyCaixa::class.java)
                        if (item.valor != null) {
                            val valor = item.valor

                            soma += valor!!
                            Log.d("TAG", "getNumerosEntradas: ${soma}  ")
                            result.invoke(
                                UiState.Success(
                                    soma
                                )
                            )
                        } else {
                            result.invoke(
                                UiState.Failure(
                                    "result null"
                                )
                            )
                        }

                    }
            }
            }.addOnFailureListener {
                result.invoke(UiState.Failure(
                    it.localizedMessage
                ))
            }

    }

    override fun getSaidas(mes: String,result: (UiState<ArrayList<MyCaixa>>) -> Unit) {
        val document = database.collection("Caixa")
            .document("Saida")
            .collection(mes)
        document.addSnapshotListener { value, error ->
            if (value != null){
                val listSaida = arrayListOf<MyCaixa>()
                val boxSaida  = value.toObjects(MyCaixa::class.java)
                listSaida.addAll(boxSaida)
                Log.d("TAG", "getEntradasRepository: $boxSaida ")
                result.invoke(UiState.Success(
                    listSaida
                ))
            }else{
                result.invoke(UiState.Failure(
                    error?.localizedMessage
                ))
            }
        }
    }

    override fun getNumerosSaidas(mes: String, result: (UiState<Any>) -> Unit) {
        val document = database.collection("Caixa")
            .document("Saida")
            .collection(mes)
        document
            .get()
            .addOnSuccessListener {
                var somaSaida = 0

                if (it.isEmpty) {
                    UiState.Failure(
                        "Campo vazio!"
                    )
                } else{
                    for (doc in it) {
                        val itemSaida = doc.toObject(MyCaixa::class.java)
                        if (itemSaida.valor != null) {
                            val valorSaida = itemSaida.valor

                            somaSaida += valorSaida!!
                            Log.d("TAG", "getNumerosEntradas: ${somaSaida}  ")
                            result.invoke(
                                UiState.Success(
                                    somaSaida
                                )
                            )
                        } else {
                            result.invoke(
                                UiState.Failure(
                                    "result null"
                                )
                            )
                        }

                    }
            }
            }.addOnFailureListener {
                result.invoke(UiState.Failure(
                    it.localizedMessage
                ))
            }

    }
}