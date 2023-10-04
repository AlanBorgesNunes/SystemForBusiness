package com.app.systemforbusiness.onboarding.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.systemforbusiness.MainActivity
import com.app.systemforbusiness.R
import com.app.systemforbusiness.databinding.ActivityLoginBinding
import com.app.systemforbusiness.utils.UiState
import com.app.systemforbusiness.utils.hide
import com.app.systemforbusiness.utils.isValidEmail
import com.app.systemforbusiness.utils.show
import com.app.systemforbusiness.utils.toast
import com.app.systemforbusiness.viewMoldes.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    val viewModelAuth: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        irCadastro()
        binding.btnLogin.setOnClickListener {
            if (validation()){
                viewModelAuth.login(
                    email = binding.txtEmail.text.toString(),
                    password = binding.txtSenha.text.toString()
                )
            }
        }
        observers()
    }

    private fun validation(): Boolean {
        var isValid = true

        if (binding.txtEmail.text.toString().isNullOrEmpty()){
            isValid = false
            toast("Email em branco!!")
        }else{
            if (!binding.txtEmail.text.toString().isValidEmail()){
                isValid = false
                toast("Email não cadastrado ou invalido!")
            }
        }
        if (binding.txtSenha.text.toString().isNullOrEmpty()){
            isValid = false
            toast("Senha em branco!!")
        }else{
            if (binding.txtSenha.text.toString().length < 8){
                isValid = false
                toast("Senha com menos de 8 digitos!")
            }
        }

        return isValid
    }

    private fun observers(){
        viewModelAuth.login.observeForever {state->
            when(state){
                is UiState.Failure -> {
                    binding.progressLogin.hide()
                    binding.txtBuscar.text = "Login"
                    toast(state.error)
                }
                UiState.Loading -> {
                    binding.txtBuscar.text = ""
                    binding.progressLogin.show()
                }
                is UiState.Success -> {
                    binding.txtBuscar.text = "Login"
                    binding.progressLogin.hide()
                    toast(state.data)
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }
            }
        }

    }
    private fun irCadastro() {
        binding.irCadastrar.setOnClickListener {
            startActivity(Intent(this, CadastreActivity::class.java))
        }
    }
}