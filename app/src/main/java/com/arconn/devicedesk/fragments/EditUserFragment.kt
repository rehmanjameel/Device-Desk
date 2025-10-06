package com.arconn.devicedesk.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.arconn.devicedesk.R
import com.arconn.devicedesk.databinding.FragmentEditUserBinding
import com.arconn.devicedesk.helpers.Resource
import com.arconn.devicedesk.helpers.RetrofitHelper
import com.arconn.devicedesk.repository.UserRepository
import com.arconn.devicedesk.utils.Validator
import com.arconn.devicedesk.viewmodel.UserViewModelFactory
import com.arconn.devicedesk.viewmodel.UsersViewModel

class EditUserFragment : Fragment() {

    private val args: EditUserFragmentArgs by navArgs()
    private lateinit var binding: FragmentEditUserBinding
    private val validator = Validator()

    private val userViewModel: UsersViewModel by viewModels {
        UserViewModelFactory(UserRepository(RetrofitHelper.apiService))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditUserBinding.inflate(layoutInflater, container, false)

        binding.userNameTIET.setText(args.userName)
        binding.userEmailTIET.setText(args.email)

        binding.editButton.setOnClickListener {
            verifyFields()
        }

        binding.backButton.setOnClickListener {
            goBack()
        }

        return binding.root
    }

    private fun verifyFields() {
        val userName = binding.userNameTIET.text.toString()
        val userEmail = binding.userEmailTIET.text.toString()

        if (userName.isEmpty() && userEmail.isEmpty()) {
            binding.userNameTIET.error = "Field required"
            binding.userEmailTIET.error = "Field required"
        } else if (userName.isEmpty()) {
            binding.userNameTIET.error = "Field required"

        } else if (userEmail.isEmpty() || !validator.isValidMail(userEmail)) {
            binding.userEmailTIET.error = "Field required or invalid email"

        } else {
            editUser(userName, userEmail)
        }
    }

    private fun editUser(userName: String, userEmail: String) {

        userViewModel.userUpdate.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> Log.e("loading", "some loading")
                is Resource.Success -> {
                    val updatedUser = state.data
                    Log.e("edited user", updatedUser.toString())
                    goBack()
                }
                is Resource.Error -> {
                    val error = state.message
                    Log.e("error message", error)
                }
            }
        }

        userViewModel.updateUser(args.id, userName, userEmail)

    }

    private fun goBack() {
        findNavController().navigate(R.id.action_editUserFragment_to_usersFragment,
            null,
            NavOptions.Builder()
                .setPopUpTo(R.id.usersFragment, true)
                .build())
    }

}