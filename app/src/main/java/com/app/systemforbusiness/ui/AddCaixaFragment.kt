package com.app.systemforbusiness.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.app.systemforbusiness.R
import com.app.systemforbusiness.databinding.FragmentAddCaixaBinding
import com.app.systemforbusiness.models.MyCaixa
import com.app.systemforbusiness.utils.UiState
import com.app.systemforbusiness.utils.toast
import com.app.systemforbusiness.viewMoldes.ViewModelCaixa
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCaixaFragment : Fragment() {
    private lateinit var binding: FragmentAddCaixaBinding
    val viewModel: ViewModelCaixa by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddCaixaBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }

       /* viewModel.getEntradas.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Failure -> {
                    toast(state.error)
                }
                UiState.Loading -> {
                    toast("esperando")
                }
                is UiState.Success -> {
                    Log.d("TAG", "onCreateView: ${state.data}")
                    toast("${state.data.indices.forEachIndexed { index, i ->  }}")
                }
            }

        }


        viewModel.getEntradas()*/

        viewModel.getNumerosEntradas.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Failure -> {
                    toast(state.error)
                }
                UiState.Loading -> {

                }
                is UiState.Success -> {
                    binding.caixaUu.text = state.data.toString()
                }
            }
        }
        viewModel.getNumerosEntradas()

        return binding.root
    }

}