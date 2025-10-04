package com.arconn.devicedesk.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arconn.devicedesk.R
import com.arconn.devicedesk.databinding.FragmentEditUserBinding

class EditUserFragment : Fragment() {

    private lateinit var binding: FragmentEditUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditUserBinding.inflate(layoutInflater, container, false)


        return binding.root
    }


}