package com.app.systemforbusiness.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.systemforbusiness.adapters.AdapterListClientes
import com.app.systemforbusiness.databinding.FragmentAddPersonaBinding
import com.app.systemforbusiness.models.Cliente
import com.app.systemforbusiness.utils.*
import com.app.systemforbusiness.viewMoldes.ClientViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPersonaFragment : Fragment(), AdapterListClientes.ClickMaisInfoClient {
    private lateinit var binding: FragmentAddPersonaBinding
    lateinit var recyclerViewListClientes: RecyclerView
    lateinit var adapterListClientes: AdapterListClientes
    lateinit var listClientes: ArrayList<Cliente>
    val clientViewModel: ClientViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPersonaBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }
        getClientList()
        observer()
        addNewClient()
        searchListClient()
        return binding.root
    }


    private fun addNewClient(){
        binding.addNewClient.setOnClickListener {
            replaceFragment(AddNewClientFragment())
        }
    }
    private fun getClientList(){
        recyclerViewListClientes = binding.recyclerListClientes
        listClientes = arrayListOf<Cliente>()
        clientViewModel.getClient(Cliente())
    }
    private fun observer(){
        clientViewModel.getClient.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Failure -> {
                    toast(state.error)
                    binding.progressListClient.hide()
                }
                UiState.Loading -> {
                    binding.txtCadastreClient.hide()
                    binding.progressListClient.show()
                }
                is UiState.Success -> {
                    if (state.data.isNullOrEmpty()) {
                        binding.progressListClient.hide()
                        binding.txtCadastreClient.show()
                    }else{
                        listClientes.addAll(state.data)
                        adapterListClientes = AdapterListClientes(listClientes, this)
                        recyclerViewListClientes.show()
                        binding.progressListClient.hide()
                        binding.txtCadastreClient.hide()
                        recyclerViewListClientes.layoutManager = LinearLayoutManager(requireContext())
                        recyclerViewListClientes.adapter = adapterListClientes
                    }

                }
            }
        }
    }

    private fun searchListClient(){
        binding.searchListClients.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                adapterListClientes.search(query)
                return true
            }


        })
    }

    override fun clickMaisInfoClient(cliente: Cliente) {
        val bundle = bundleOf(
            "name" to cliente.name,
            "email" to cliente.email,
            "genero" to cliente.genero,
            "number" to cliente.number,
            "date" to cliente.date,
            "idClient" to cliente.idClient,
            "photo" to cliente.photo
        )
        val fragment = ProfileClientFragment()
        fragment.arguments = bundle
        replaceFragment(fragment)
    }

}