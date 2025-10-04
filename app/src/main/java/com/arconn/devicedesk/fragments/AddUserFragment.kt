package com.arconn.devicedesk.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.arconn.devicedesk.R
import com.arconn.devicedesk.databinding.FragmentAddUserBinding
import com.arconn.devicedesk.helpers.Resource
import com.arconn.devicedesk.helpers.RetrofitHelper
import com.arconn.devicedesk.repository.UserRepository
import com.arconn.devicedesk.utils.Validator
import com.arconn.devicedesk.viewmodel.UserViewModelFactory
import com.arconn.devicedesk.viewmodel.UsersViewModel

class AddUserFragment : Fragment() {

    private lateinit var binding: FragmentAddUserBinding

    private val validator = Validator()

    private val userViewModel: UsersViewModel by viewModels {
        UserViewModelFactory(UserRepository(RetrofitHelper.apiService))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddUserBinding.inflate(layoutInflater, container, false)

        binding.addButton.setOnClickListener {
            verifyFields()
        }

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_addUserFragment_to_usersFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.usersFragment, true)
                    .build())
        }

        return binding.root
    }

    private fun verifyFields() {
        val userName = binding.userNameTIET.text.toString()
        val userEmail = binding.userEmailTIET.text.toString()
        val password = binding.passwordTIET.text.toString()

        if (userName.isEmpty() && userEmail.isEmpty() && password.isEmpty()) {
            binding.userNameTIET.error = "Field required"
            binding.userEmailTIET.error = "Field required"
            binding.passwordTIET.error = "Field required"
        } else if (userName.isEmpty()) {
            binding.userNameTIET.error = "Field required"

        } else if (userEmail.isEmpty() || !validator.isValidMail(userEmail)) {
            binding.userEmailTIET.error = "Field required or invalid email"

        } else if (password.isEmpty()) {
            binding.passwordTIET.error = "Field required"

        } else {
            addUser(userName, userEmail, password)
        }
    }

    private fun addUser(userName: String, userEmail: String, password: String) {

        userViewModel.userCreation.observe(viewLifecycleOwner) {state ->
            when(state) {
                is Resource.Loading<*> -> Log.e("loading", "some loading")

                is Resource.Success -> {
                    val createdUser = state
                    findNavController().navigate(R.id.action_addUserFragment_to_usersFragment,
                        null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.usersFragment, true)
                            .build())
                    Log.e("success message", createdUser.data.toString())

                }
                is Resource.Error -> {
                    val error = state.message
                    Log.e("error message", error)
                }
            }
        }
        userViewModel.createUser(userName, email = userEmail, password)

    }

}