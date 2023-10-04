package com.app.systemforbusiness.onboarding.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.app.systemforbusiness.R
import com.app.systemforbusiness.databinding.ActivityCadastreBinding
import com.app.systemforbusiness.models.Usuario
import com.app.systemforbusiness.utils.UiState
import com.app.systemforbusiness.utils.hide
import com.app.systemforbusiness.utils.isValidEmail
import com.app.systemforbusiness.utils.show
import com.app.systemforbusiness.utils.toast
import com.app.systemforbusiness.viewMoldes.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CadastreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastreBinding

    val viewModelAuth: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        irLogin()
        binding.btnCadastrar.setOnClickListener {
            if (validation()){
                viewModelAuth.register(
                    email = binding.txtEmailCadastrar.text.toString(),
                    password = binding.txtSenhaCadastrar.text.toString(),
                    getUserObj()
                )
            }
        }
        observers()

    }

    private fun getUserObj(): Usuario {
        return Usuario(
            name = "",
            email = binding.txtEmailCadastrar.text.toString(),
            uid = ""
        )
    }
    private fun validation(): Boolean {
        var isValid = true

        if ( binding.txtEmailCadastrar.text.toString().isNullOrEmpty()
            || binding.txtSenhaCadastrar.text.toString().isNullOrEmpty()
        ) {
            isValid = false
            toast("Campo obrigatório inválido!")
        }

        if (!binding.txtEmailCadastrar.text.toString().isValidEmail()) {
            isValid = false
            toast("Email inválido!")
        }
        if (binding.txtSenhaCadastrar.text.toString().length < 8) {
            isValid = false
            toast("A senha deve conter no minimo 8 caracteres!")
        }

        return isValid
    }

    private fun observers(){
        viewModelAuth.register.observeForever {state->
            when(state){
                is UiState.Failure -> {
                    binding.progressCadastrar.hide()
                    binding.txtCadastrar.text = "Cadastrar"
                    toast(state.error)
                }
                UiState.Loading -> {
                    binding.txtCadastrar.text = ""
                    binding.progressCadastrar.show()
                }
                is UiState.Success -> {
                    binding.txtCadastrar.text = "Cadastrar"
                    binding.progressCadastrar.hide()
                    toast(state.data)
                }
            }

        }
    }
    private fun irLogin(){
        binding.irLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}