package com.arconn.devicedesk

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.arconn.devicedesk.databinding.ActivityAddUserBinding
import com.arconn.devicedesk.helpers.Resource
import com.arconn.devicedesk.helpers.RetrofitHelper
import com.arconn.devicedesk.repository.UserRepository
import com.arconn.devicedesk.utils.Validator
import com.arconn.devicedesk.viewmodel.UserViewModelFactory
import com.arconn.devicedesk.viewmodel.UsersViewModel
import kotlin.getValue

class AddUser : AppCompatActivity() {
    private lateinit var binding: ActivityAddUserBinding
    private val validator = Validator()

    private val usersViewModel: UsersViewModel by viewModels {
        UserViewModelFactory(UserRepository(RetrofitHelper.apiService))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.addButton.setOnClickListener {
            verifyFields()
        }
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

        } else if (userEmail.isEmpty() || validator.isValidMail(userEmail)) {
            binding.userEmailTIET.error = "Field required or invalid email"

        } else if (password.isEmpty()) {
            binding.passwordTIET.error = "Field required"

        } else {
            addUser(userName, userEmail, password)
        }
    }

    private fun addUser(userName: String, userEmail: String, password: String) {

        usersViewModel.userCreation.observe(this) {state ->
            when(state) {
                is Resource.Loading<*> -> Log.e("loading", "some loading")

                is Resource.Success -> {
                    val createdUser = state
                    Log.e("success message", createdUser.data.toString())

                    onBackPressed()
                }
                is Resource.Error -> {
                    val error = state.message
                    Log.e("error message", error)
                }
            }
        }
        usersViewModel.createUser(userName, email = userEmail, password)


    }
}