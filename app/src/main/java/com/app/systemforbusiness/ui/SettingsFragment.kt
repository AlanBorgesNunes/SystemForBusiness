package com.app.systemforbusiness.ui

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.app.systemforbusiness.alarm.AlarmAniversario
import com.app.systemforbusiness.databinding.FragmentSettingsBinding
import com.app.systemforbusiness.utils.replaceFragment
import com.app.systemforbusiness.utils.toast
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }

        binding.btnWorkers.setOnClickListener {
            replaceFragment(AddNewColaboratorFragment())
        }
        return binding.root
    }

}