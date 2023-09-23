package com.app.systemforbusiness.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.systemforbusiness.databinding.ItemListWorkersBinding
import com.app.systemforbusiness.models.Workers

class AdapterListWorkers(val list: List<Workers>,
                         val clickWorkers: ClickWorkers) :
    RecyclerView.Adapter<AdapterListWorkers.ViewHolder>() {

    interface ClickWorkers{
        fun clickWorkers(workers: Workers)
    }

    inner class ViewHolder(val binding: ItemListWorkersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Workers) {
            binding.receiveNameWorker.text = item.name
            binding.receiveCargoWorker.text = item.cargo

            binding.btnVerMaisWorkers.setOnClickListener {
                clickWorkers.clickWorkers(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemListWorkersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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