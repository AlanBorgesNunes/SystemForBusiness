package com.app.systemforbusiness.repository

import android.util.Log
import com.app.systemforbusiness.models.MyCaixa
import com.app.systemforbusiness.utils.UiState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class CaixaRepositoryImp(
    val database: FirebaseFirestore,
) : CaixaRepository {


    override fun getEntradas( result: (UiState<ArrayList<MyCaixa>>) -> Unit) {
        val document = database.collection("Entradas")
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

    override fun getNumerosEntradas(result: (UiState<ArrayList<Int>>) -> Unit) {
        val document = database.collection("Entradas")
        document
            .get()
            .addOnSuccessListener {
                val valores = arrayListOf<Int>()

                for (doc in it){
                    val item = doc.toObject(MyCaixa::class.java)
                    if (item != null){
                        valores.add(item.valor!!)
                        result.invoke(UiState.Success(
                            valores
                        ))
                    }else{
                        result.invoke(UiState.Failure(
                            "result null"
                        ))
                    }

                }
            }.addOnFailureListener {
                result.invoke(UiState.Failure(
                    it.localizedMessage
                ))
            }

    }
}