package com.app.systemforbusiness.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.core.view.get
import androidx.fragment.app.viewModels
import com.app.systemforbusiness.R
import com.app.systemforbusiness.alarm.AlarmAniversario
import com.app.systemforbusiness.databinding.FragmentAddNewClientBinding
import com.app.systemforbusiness.models.Cliente
import com.app.systemforbusiness.utils.*
import com.app.systemforbusiness.viewMoldes.ClientViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.api.Billing
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.nio.file.attribute.AclEntry.Builder
import java.util.*

@AndroidEntryPoint
class AddNewClientFragment : Fragment() {
    private lateinit var binding: FragmentAddNewClientBinding
    val viewModelClient: ClientViewModel by viewModels()
    var dataL: Long? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    var selecPhotoUri: Uri? = null
    private lateinit var mProgressDialog: ProgressDialog
    private lateinit var imgPerfil: String
    private lateinit var calendar: Calendar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddNewClientBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backToCustomers()
        observer()
        binding.escolheData.setOnClickListener {
            showDataRangePicker()
        }
        binding.addPhoto.setOnClickListener {
            openCamera()
        }
        binding.btnSaveCliente.setOnClickListener {
            if (validation()){
                viewModelClient.addClient(
                    getClientObj()
                )
            }
        }
        createNotificationChannel()
    }

    @SuppressLint("ResourceAsColor")
    private fun showDataRangePicker() {
        val dateRangePicker =
            MaterialDatePicker
                .Builder.datePicker()
                .setTitleText("Data do seu aniversário!")
                .build()


        dateRangePicker.show(
            activity?.supportFragmentManager!!,
            "date_range_picker"
        )


        dateRangePicker.addOnPositiveButtonClickListener { dateSelected->
            binding.escolheData.text = convertLongToTime(dateSelected)
            dataL = dateSelected
            scheduleBirthdayAlarm(dateSelected)
        }
    }

    private fun backToCustomers(){
        binding.backToCustomers.setOnClickListener {
            replaceFragment(AddPersonaFragment())
        }
    }

    private fun validation(): Boolean{
        var isValid = true
        if (binding.edtName.text.toString().isNullOrEmpty()){
            isValid = false
            toast("Name invalid!")
        }
        if (binding.edtEmail.text.toString().isNullOrEmpty()){
            isValid = false
            toast("Email invalid!")
        }
        if (binding.edtNumber.text.toString().isNullOrEmpty() || binding.edtNumber.text.toString().length < 8){
            isValid = false
            toast("Number invalid!")
        }

        return isValid
    }
    private fun getClientObj():Cliente{
        return Cliente(
            name = binding.edtName.text.toString(),
            email = binding.edtEmail.text.toString(),
            number = binding.edtNumber.text.toString(),
            date = dataL.toString(),
            genero = when(binding.generoSpinner.selectedItem){
                "Masculino" -> {"Masculino"}
                "Feminino" -> {"Feminino"}
                "Prefiro não dizer" -> {"Prefiro não dizer"}
                else -> {}
            },
            photo = imgPerfil
        )
    }

    private fun observer(){
        viewModelClient.addClient.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Failure -> {
                    binding.saveClienteProgress.hide()
                    binding.txtSave.show()
                    toast(state.error)
                }
                UiState.Loading -> {
                    binding.txtSave.hide()
                    binding.saveClienteProgress.show()
                }
                is UiState.Success -> {
                    binding.saveClienteProgress.hide()
                    binding.txtSave.show()
                    binding.edtName.setText("")
                    binding.edtEmail.setText("")
                    binding.edtNumber.setText("")
                    binding.generoSpinner[0]
                    toast(state.data)
                }
            }
        }
    }

    private fun openCamera(){
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, 1)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null){

            val bip = data.extras?.get("data") as Bitmap

            binding.recebeImagePerrfil.setImageBitmap(bip)
            mProgressDialog = ProgressDialog(requireContext())
            mProgressDialog.setMessage("Carregando imagem...")
            mProgressDialog.show()
            subirImagensPostsStorege(bip)
        }else{
            toast("deu ruim na foto")
        }
    }

    private fun subirImagensPostsStorege(imageBitmap: Bitmap) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imagesRef= storageRef.child("images")
        val imageFileName = UUID.randomUUID().toString()
        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val data = baos.toByteArray()

        val imageRef = imagesRef.child(imageFileName)
        val uploadTask = imageRef.putBytes(data)

        uploadTask.addOnSuccessListener {
            mProgressDialog.setMessage("upload da foto!")
            mProgressDialog.show()
            imageRef.downloadUrl.addOnSuccessListener {
                mProgressDialog.setMessage("baixando a foto!")
                mProgressDialog.show()
                if (it != null){
                    mProgressDialog.dismiss()
                }
                imgPerfil = it.toString()
            }.addOnFailureListener {
                toast("problema no download, tente novamente!")
            }
        }.addOnFailureListener {
            toast("problema no upload, tente novamente!")
        }



    }

    private fun scheduleBirthdayAlarm(birthdayDateMillis: Long) {
        val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmAniversario::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            0,
            intent,
            0
        )

        // Agende o alarme para a data de aniversário

        Log.d("dateMili", "scheduleBirthdayAlarm: $birthdayDateMillis ")
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            birthdayDateMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent)
    }

    private fun createNotificationChannel(){
     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
         val name : CharSequence = "Alan Borges"
         val description = "Lindo"
         val importance = NotificationManager.IMPORTANCE_HIGH
         val channel = NotificationChannel("channelID",name,importance)
         channel.description = description
         val notificationManager = activity?.getSystemService(
             NotificationManager::class.java
         )

         notificationManager?.createNotificationChannel(channel)
     }
    }

}