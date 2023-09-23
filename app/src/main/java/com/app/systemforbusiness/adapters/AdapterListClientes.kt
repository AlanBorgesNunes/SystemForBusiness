package com.app.systemforbusiness.adapters

import android.app.Activity
import android.app.Activity.*
import android.app.AlarmManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivities
import androidx.recyclerview.widget.RecyclerView
import com.app.systemforbusiness.databinding.ItemListClienteBinding
import com.app.systemforbusiness.models.Cliente
import com.app.systemforbusiness.utils.scheduleBirthdayAlarm
import com.squareup.picasso.Picasso

class AdapterListClientes(val list: List<Cliente>,
val clickMaisInfoClient: ClickMaisInfoClient) :
RecyclerView.Adapter<AdapterListClientes.ViewHolder>(){

    private var listL = list.toMutableList()
    lateinit var alarmManager: AlarmManager

    interface ClickMaisInfoClient{
        fun clickMaisInfoClient(cliente: Cliente)
    }

    inner class ViewHolder(val binding: ItemListClienteBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Cliente){
            binding.receiveNameCliente.text = item.name
            Picasso.get().load(item.photo).into(binding.recebeImageListClient)
            binding.btnVerMaisClient.setOnClickListener {
                clickMaisInfoClient.clickMaisInfoClient(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemListClienteBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = listL[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return listL.size
    }

    fun search(query: String): Boolean{
        listL.clear()
        listL.addAll(list.filter { it.name!!.contains(query, true)  })
        notifyDataSetChanged()
        return listL.isEmpty()
    }

    fun clearSearch(){
        listL = list.toMutableList()
        notifyDataSetChanged()
    }
}