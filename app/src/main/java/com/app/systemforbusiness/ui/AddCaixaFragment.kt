package com.app.systemforbusiness.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.datastore.preferences.core.emptyPreferences
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.systemforbusiness.adapters.AdapterCaixaEntradas
import com.app.systemforbusiness.adapters.AdapterCaixaSaidas
import com.app.systemforbusiness.databinding.FragmentAddCaixaBinding
import com.app.systemforbusiness.models.MyCaixa
import com.app.systemforbusiness.utils.*
import com.app.systemforbusiness.viewMoldes.ViewModelCaixa
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCaixaFragment : Fragment() {
    private lateinit var binding: FragmentAddCaixaBinding
    val viewModel: ViewModelCaixa by viewModels()
    val listEntradas = arrayListOf<MyCaixa>()
    val listSaidas = arrayListOf<MyCaixa>()

    var entradas = String()
    var saidass = String()

    val adapterSaidas by lazy {
        AdapterCaixaSaidas(listSaidas)
    }
    val adapterEntradas by lazy {
        AdapterCaixaEntradas(listEntradas)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddCaixaBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }

        oberservers()
        initSpinner()

       binding.btnGetResultCaixa.setOnClickListener {
           getResults()
       }
        return binding.root
    }

    private fun initSpinner() {
        spinnerFun(requireContext(), binding.spinnerLolo, lists.listMeses)
    }

    private fun oberservers() {
        viewModel.getSaidas.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Failure -> {
                    toast(state.error)
                }
                UiState.Loading -> {
                    listSaidas.clear()
                }
                is UiState.Success -> {

                    if (state.data.isEmpty()) {
                        toast("Ainda não temos lançamentos pra esse mês!")
                    } else {
                        listSaidas.addAll(state.data)
                        binding.rvReceiveEntradasSaidas.show()
                        binding.rvReceiveEntradasSaidas.layoutManager =
                            LinearLayoutManager(requireContext())
                        binding.rvReceiveEntradasSaidas.adapter = adapterSaidas
                    }
                }
            }
        }

        viewModel.getNumerosSaidas.observe(viewLifecycleOwner) { stateSaidas ->
            when (stateSaidas) {
                is UiState.Failure -> {
                    binding.progressUiui.hide()
                    toast(stateSaidas.error)
                }
                UiState.Loading -> {
                    binding.caixaSaidas.hide()
                    binding.progressUiui.show()
                }
                is UiState.Success -> {
                    binding.progressUiui.hide()
                    if (stateSaidas.data.toString().isEmpty()) {

                    } else {
                        saidass = stateSaidas.data.toString()
                        binding.progressUiui.hide()
                        binding.caixaSaidas.show()
                        binding.caixaSaidas.text = numberCurrency(stateSaidas.data.toString())
                    }
                }
            }
        }

        viewModel.getEntradas.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Failure -> {
                    toast(state.error)
                }
                UiState.Loading -> {
                    listEntradas.clear()
                }
                is UiState.Success -> {
                    if (state.data.isEmpty()) {
                        toast("Ainda não temos lançamentos pra esse mês!")
                    } else {
                        listEntradas.addAll(state.data)
                        binding.rvReceiveEntradasEntradas.show()
                        binding.rvReceiveEntradasEntradas.layoutManager =
                            LinearLayoutManager(requireContext())
                        binding.rvReceiveEntradasEntradas.adapter = adapterEntradas
                    }

                }
            }

        }


        viewModel.getNumerosEntradas.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Failure -> {
                    binding.progressUiui.hide()
                    toast(state.error)
                }
                UiState.Loading -> {
                    binding.caixaEntradas.hide()
                    binding.progressUiui.show()
                    binding.totalMeu.text = "Aguarde..."
                    binding.receivePorcentagem.text = "Aguarde..."
                }
                is UiState.Success -> {
                    Log.d("TAG", "onCreateView: ${state.data}")
                    binding.progressUiui.hide()
                    if (state.data.toString().isEmpty()) {
                        binding.progressUiui.hide()
                    } else {
                        entradas = state.data.toString()
                        binding.progressUiui.hide()
                        binding.caixaEntradas.show()
                        binding.caixaEntradas.text = numberCurrency(state.data.toString())


                        Handler(Looper.myLooper()!!).postDelayed({
                            if (entradas.isEmpty() || saidass.isEmpty()) {
                                binding.totalMeu.text = "Recarregue a pagina!"
                            } else {
                                val resultado = entradas.toInt() - saidass.toInt()
                                val porcentagem = entradas.toInt() * 0.3
                                val resultTotal = resultado - porcentagem
                                binding.totalMeu.text = numberCurrency(resultTotal.toString())
                                binding.receivePorcentagem.text =
                                    numberCurrency(porcentagem.toString())
                            }

                        }, 2000)

                    }

                }
            }
        }
    }

    private fun getResults() {
        when (binding.spinnerLolo.text.toString()) {
            "Mês janeiro" -> {
                viewModel.getEntradas("Mês Janeiro")
                viewModel.getNumerosEntradas("Mês Janeiro")
                viewModel.getNumerosSaidas("Mês Janeiro")
                viewModel.getSaidas("Mês Janeiro")
            }
            "Mês fevereiro" -> {
                viewModel.getEntradas("Mês fevereiro")
                viewModel.getNumerosEntradas("Mês fevereiro")
                viewModel.getNumerosSaidas("Mês fevereiro")
                viewModel.getSaidas("Mês fevereiro")
            }
            "Mês março" -> {
                viewModel.getEntradas("Mês março")
                viewModel.getNumerosEntradas("Mês março")
                viewModel.getNumerosSaidas("Mês março")
                viewModel.getSaidas("Mês março")
            }
            "Mês abril" -> {
                viewModel.getEntradas("Mês abril")
                viewModel.getNumerosEntradas("Mês abril")
                viewModel.getNumerosSaidas("Mês abril")
                viewModel.getSaidas("Mês abril")
            }
            "Mês maio" -> {
                viewModel.getEntradas("Mês maio")
                viewModel.getNumerosEntradas("Mês maio")
                viewModel.getNumerosSaidas("Mês maio")
                viewModel.getSaidas("Mês maio")
            }
            "Mês junho" -> {
                viewModel.getEntradas("Mês junho")
                viewModel.getNumerosEntradas("Mês junho")
                viewModel.getNumerosSaidas("Mês junho")
                viewModel.getSaidas("Mês junho")
            }
            "Mês julho" -> {
                viewModel.getEntradas("Mês julho")
                viewModel.getNumerosEntradas("Mês julho")
                viewModel.getNumerosSaidas("Mês julho")
                viewModel.getSaidas("Mês julho")
            }
            "Mês agosto" -> {
                viewModel.getEntradas("Mês agosto")
                viewModel.getNumerosEntradas("Mês agosto")
                viewModel.getNumerosSaidas("Mês agosto")
                viewModel.getSaidas("Mês agosto")
            }
            "Mês setembro" -> {
                viewModel.getEntradas("Mês setembro")
                viewModel.getNumerosEntradas("Mês setembro")
                viewModel.getNumerosSaidas("Mês setembro")
                viewModel.getSaidas("Mês setembro")
            }
            "Mês outubro" -> {
                viewModel.getEntradas("Mês outubro")
                viewModel.getNumerosEntradas("Mês outubro")
                viewModel.getNumerosSaidas("Mês outubro")
                viewModel.getSaidas("Mês outubro")
            }
            "Mês novembro" -> {
                viewModel.getEntradas("Mês novembro")
                viewModel.getNumerosEntradas("Mês novembro")
                viewModel.getNumerosSaidas("Mês novembro")
                viewModel.getSaidas("Mês novembro")
            }
            "Mês dezembro" -> {
                viewModel.getEntradas("Mês dezembro")
                viewModel.getNumerosEntradas("Mês dezembro")
                viewModel.getNumerosSaidas("Mês dezembro")
                viewModel.getSaidas("Mês dezembro")
            }

        }
    }

}