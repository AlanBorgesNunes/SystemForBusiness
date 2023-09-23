package com.app.systemforbusiness.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.systemforbusiness.databinding.ItemListClienteBinding
import com.app.systemforbusiness.databinding.ItemUltimoProcedimentoBinding
import com.app.systemforbusiness.models.Cliente
import com.app.systemforbusiness.models.UltimosProcedimentos
import com.app.systemforbusiness.utils.convertLongToTime
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class AdapterUltimosProcedimentos(val list: List<UltimosProcedimentos>, val clienteid: String,val clickUltimoProced: ClickUltimoProced) :
    RecyclerView.Adapter<AdapterUltimosProcedimentos.ViewHolder>() {

    interface ClickUltimoProced{
        fun clickUltimoProced(ultimosProcedimentos: UltimosProcedimentos)
    }
    inner class ViewHolder(val binding: ItemUltimoProcedimentoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UltimosProcedimentos) {
            val db = Firebase.firestore
            db.collection("clientes")
                .document(clienteid)
                .collection("UltimosProcedimentos")
                .document(item.idEvent!!)
                .addSnapshotListener { value, error ->
                    if (value != null) {
                        val document = value.toObject(UltimosProcedimentos::class.java)
                        Picasso.get().load(document?.photo)
                            .into(binding.imgItemUltimosProcedimentos)
                        binding.txtDateItemUltimosProcedimentos.text = convertLongToTime(
                            document?.date?.toLong()!!
                        )
                    }
                }

            binding.imgItemUltimosProcedimentos.setOnClickListener {
                clickUltimoProced.clickUltimoProced(item)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemUltimoProcedimentoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}