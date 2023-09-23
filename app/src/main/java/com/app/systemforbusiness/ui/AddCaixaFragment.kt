package com.app.systemforbusiness.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.systemforbusiness.R
import com.app.systemforbusiness.databinding.FragmentAddCaixaBinding


class AddCaixaFragment : Fragment() {
    private lateinit var binding: FragmentAddCaixaBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddCaixaBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }



        return binding.root
    }

}