package com.app.systemforbusiness

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app.systemforbusiness.alarm.AlarmAniversario
import com.app.systemforbusiness.databinding.ActivityMainBinding
import com.app.systemforbusiness.ui.AddCaixaFragment
import com.app.systemforbusiness.ui.AddPersonaFragment
import com.app.systemforbusiness.ui.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(SettingsFragment())
        createNotificationChannel()
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.add_caixa -> {
                    replaceFragment(AddCaixaFragment())
                }
                R.id.add_cliente ->{
                    replaceFragment(AddPersonaFragment())
                }
                R.id.business ->{
                    replaceFragment(SettingsFragment())
                }
                else->{

                }
            }

            true
        }

    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManeger = supportFragmentManager
        val fragmentTransition = fragmentManeger.beginTransaction()
        fragmentTransition.replace(R.id.frame_layuot,fragment)
        fragmentTransition.commit()
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name: CharSequence = "MegaDeveloperChannel"
            val description = "Channel Alarm of Bharday"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("MegaDeveloper",name,importance)
            channel.description = description
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )

            notificationManager.createNotificationChannel(channel)
        }
    }


}