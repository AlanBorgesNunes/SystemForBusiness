package com.app.systemforbusiness.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.systemforbusiness.R
import com.app.systemforbusiness.adapters.AdapterListWorkers
import com.app.systemforbusiness.databinding.FragmentAddNewColaboratorBinding
import com.app.systemforbusiness.models.Workers
import com.app.systemforbusiness.utils.*
import com.app.systemforbusiness.viewMoldes.WorkersViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNewColaboratorFragment : Fragment(), AdapterListWorkers.ClickWorkers {
    private lateinit var binding: FragmentAddNewColaboratorBinding
    private val viewModel: WorkersViewModel by viewModels()
    private val list = arrayListOf<Workers>()
    private val adapter by lazy {
        AdapterListWorkers(
            list, this
        )
    }
    var dataL: Long? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddNewColaboratorBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }

        spinners()
        backToBusiness()
        getWorkers()
        addWorkers()
        observers()
        return binding.root
    }

    private fun addWorkers(){
        binding.btnSaveWorker.setOnClickListener {
            if (binding.nomeColaborator.text.toString().isBlank()||
                    binding.dataEntredaColaborator.text.toString().isBlank()){
                toast("Preencha os dados corretamente!!")
            }else{
            viewModel.addWorkers(
                Workers(
                    name = binding.nomeColaborator.text.toString(),
                    cargo = binding.spinnerReceiveCargos.text.toString(),
                    dataEntrada = dataL
                )
            )
            }
        }

        binding.dataEntredaColaborator.setOnClickListener {
            showDataRangePicker()
        }
    }

    private fun getWorkers(){
        viewModel.getWorkers(
            Workers()
        )
    }

    private fun showDataRangePicker() {
        val dateRangePicker =
            MaterialDatePicker
                .Builder.datePicker()
                .setTitleText("Data de entrada!")
                .build()


        dateRangePicker.show(
            activity?.supportFragmentManager!!,
            "date_range_picker"
        )


        dateRangePicker.addOnPositiveButtonClickListener { dateSelected->
            binding.dataEntredaColaborator.text = convertLongToTime(dateSelected)
            dataL = dateSelected
        }
    }

    private fun observers(){

        viewModel.addWorker.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Failure -> {
                    toast(state.error)
                }
                UiState.Loading -> {
                    binding.txtSaveWorker.hide()
                    binding.saveWorkerProgress.show()
                }
                is UiState.Success -> {
                    binding.saveWorkerProgress.hide()
                    binding.txtSaveWorker.show()
                    toast(state.data)
                }
            }
        }

        viewModel.getWorkers.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Failure -> {
                    binding.progressRecebeWorkers.hide()
                    binding.txtMsgFataDeWorkers.show()
                }
                UiState.Loading -> {
                    binding.progressRecebeWorkers.show()
                }
                is UiState.Success -> {
                    binding.progressRecebeWorkers.hide()
                    if (state.data.isNullOrEmpty()){

                        binding.txtMsgFataDeWorkers.show()

                    }else{
                        binding.nomeColaborator.text.clear()
                        binding.dataEntredaColaborator.text = "Data de entrada"
                        binding.rvColaborators.show()
                        list.clear()
                        list.addAll(state.data)
                        binding.rvColaborators.layoutManager = LinearLayoutManager(requireContext())
                        binding.rvColaborators.adapter = adapter
                    }
                }
            }
        }
    }

    private fun spinners() {
        spinnerFun(requireContext(), binding.spinnerReceiveCargos, lists.listCargos)
    }

    private fun backToBusiness(){
        binding.backToBisuness.setOnClickListener {
            replaceFragment(SettingsFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManeger = activity?.supportFragmentManager
        val fragmentTransition = fragmentManeger?.beginTransaction()
        fragmentTransition?.replace(R.id.frame_layuot,fragment)
        fragmentTransition?.commit()
    }

    override fun clickWorkers(workers: Workers) {
        val bundle = bundleOf(
            "nome" to workers.name,
            "cargo" to workers.cargo,
            "data" to workers.dataEntrada
        )

        val fragment = DetalhesForWorkersFragment()
        fragment.arguments = bundle
        replaceFragment(fragment)
    }

}