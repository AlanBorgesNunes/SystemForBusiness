package com.app.systemforbusiness.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.systemforbusiness.databinding.FragmentDetalhesForWorkersBinding
import com.app.systemforbusiness.utils.replaceFragment
import com.app.systemforbusiness.utils.toPrettyDate

class DetalhesForWorkersFragment : Fragment() {
    private lateinit var binding: FragmentDetalhesForWorkersBinding

    var nome: String? = null
    var cargo: String? = null
    var data: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetalhesForWorkersBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }
        receiveArgs()
        back()
        return binding.root
    }

    private fun back(){
        binding.backToWorkers.setOnClickListener {
            val fragment = AddNewColaboratorFragment()
            replaceFragment(fragment)
        }
    }

    private fun receiveArgs(){
        nome = requireArguments().getString("nome")
        cargo = requireArguments().getString("cargo")
        data = requireArguments().getLong("data").toPrettyDate()
        binding.receiveNomePageWorker.text = nome
        binding.receiveCargoPageWorker.text = cargo
        binding.receiveDataPageWorker.text  = data
    }
}