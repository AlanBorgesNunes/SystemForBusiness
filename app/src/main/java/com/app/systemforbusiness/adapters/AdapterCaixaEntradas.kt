package com.app.systemforbusiness.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.systemforbusiness.databinding.ItemCaixaBinding
import com.app.systemforbusiness.models.MyCaixa
import com.app.systemforbusiness.utils.numberCurrency

class AdapterCaixaEntradas(val list: ArrayList<MyCaixa>) : RecyclerView.Adapter<AdapterCaixaEntradas.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCaixaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyCaixa) {
            binding.receiveTipo.text = item.tipo.toString()
            binding.receiveValor.text = numberCurrency(item.valor.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemCaixaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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