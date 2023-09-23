package com.app.systemforbusiness.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.systemforbusiness.R
import com.app.systemforbusiness.adapters.AdapterUltimosProcedimentos
import com.app.systemforbusiness.databinding.FragmentProfileClientBinding
import com.app.systemforbusiness.models.UltimosProcedimentos
import com.app.systemforbusiness.utils.*
import com.app.systemforbusiness.viewMoldes.ClientViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLEncoder

@AndroidEntryPoint
class ProfileClientFragment : Fragment(), AdapterUltimosProcedimentos.ClickUltimoProced {
    private lateinit var binding: FragmentProfileClientBinding
    private lateinit var message: String
    private lateinit var numero: String
    private lateinit var photo: String
    private lateinit var idClient: String
    private lateinit var dialog: AlertDialog
    private lateinit var adapterUltimosProcedimentos: AdapterUltimosProcedimentos
    private lateinit var recyclerView: RecyclerView
    private lateinit var list: ArrayList<UltimosProcedimentos>
    val clientViewModel: ClientViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileClientBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }
        recebeDados()
        backToCostumers()
        chamarWhatsApp()
        getRecycler()
        observer()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun recebeDados() {
        numero = requireArguments().getString("number").toString()
        photo = requireArguments().getString("photo").toString()
        Picasso.get().load(photo).into(binding.receiveImagemPerfil)
        val date = requireArguments().getString("date")
        idClient = requireArguments().getString("idClient").toString()
        binding.receiveNomeProfile.text = requireArguments().getString("name")
        message = "Olá ${requireArguments().getString("name")}, vamos marcar sua manutenção!"
        binding.receiveEmailProfile.text = requireArguments().getString("email")
        binding.receiveNumberProfile.text = numero?.let { String().formatarNumeroTelefone(it) }
        binding.receiveDateProfile.text = date.let { convertLongToTime(date?.toLong()!!) }
        binding.receiveGeneroProfile.text = requireArguments().getString("genero")


        binding.receiveNumberProfile.setOnClickListener {
            activity?.let { it1 ->
                clipBoard(it1, binding.receiveNumberProfile.text.toString())
            }.apply {
                toast("Número Copiado!")
            }
        }
    }

    private fun chamarWhatsApp() {
        binding.btnWhatsapp.setOnClickListener {
            actionWhatsApp()
        }
    }

    @SuppressLint("IntentReset")
    private fun actionWhatsApp() {
        val url = "https://api.whatsapp.com/send?phone=$numero" + "&text=" + URLEncoder.encode(
            message,
            "UTF-8"
        )
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            toast(e.message)
        }
    }

    private fun backToCostumers() {
        binding.backToCustomers.setOnClickListener {
            replaceFragment(AddPersonaFragment())
        }
    }

    private fun getRecycler() {
        list = arrayListOf()
        adapterUltimosProcedimentos = AdapterUltimosProcedimentos(list, clienteid = idClient, this)
        recyclerView = binding.recyclerPerfilClient
        clientViewModel.getUltimosProcedimentos(idClient)
    }

    @SuppressLint("SetTextI18n")
    private fun observer() {
        clientViewModel.getUltimosProcedimentos.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Failure -> {
                    toast(state.error)
                }
                UiState.Loading -> {
                    binding.progressUltimosProceds.show()
                }
                is UiState.Success -> {
                    if (state.data.isNotEmpty()) {
                        binding.progressUltimosProceds.hide()
                        recyclerView.show()
                        list.addAll(state.data)
                        recyclerView.layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        recyclerView.adapter = adapterUltimosProcedimentos
                    } else {
                        binding.progressUltimosProceds.hide()
                        binding.txtMsgSemUltimosProce.apply {
                            show()
                            text =
                                "${requireArguments().getString("name")}, ainda não fez nenhum procedimento!!"
                        }

                    }

                }
            }
        }
    }

    override fun clickUltimoProced(ultimosProcedimentos: UltimosProcedimentos) {

        val build = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_ver_foto, null)

        build.setView(view)

        val imagem = view.findViewById<ImageView>(R.id.recebe_Image_click_post)
        val btnSair = view.findViewById<ImageView>(R.id.sair_image_post)

        Picasso.get().load(ultimosProcedimentos.photo).into(imagem)

        btnSair.setOnClickListener {
            dialog.dismiss()
        }

        dialog = build.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }


}