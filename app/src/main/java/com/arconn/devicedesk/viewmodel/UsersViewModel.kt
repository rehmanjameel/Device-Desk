package com.arconn.devicedesk.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arconn.devicedesk.helpers.Resource
import com.arconn.devicedesk.model.CreateUser
import com.arconn.devicedesk.model.UsersModel
import com.arconn.devicedesk.repository.UserRepository
import kotlinx.coroutines.launch

class UsersViewModel(private val repository: UserRepository) : ViewModel() {

    private val _users = MutableLiveData<Resource<List<UsersModel>>>()
    val users: LiveData<Resource<List<UsersModel>>> = _users

    private val _userCreation = MutableLiveData<Resource<UsersModel>>()
    val userCreation: LiveData<Resource<UsersModel>> = _userCreation

    fun loadUsers() {

        viewModelScope.launch {
            _users.value = Resource.Loading()
            try {
                val response = repository.fetchUsers()
                _users.value = Resource.Success(response)
            } catch (e: Exception) {
                _users.value = Resource.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun createUser(userName: String, email: String, password: String) {
        viewModelScope.launch {
            _userCreation.value = Resource.Loading()
            try {
                val userRequest = CreateUser(userName, email, password)
                val response = repository.createUser(userRequest)
                _userCreation.value = Resource.Success(response)
            } catch (e: Exception) {
                _userCreation.value = Resource.Error(e.message ?: "Unknown error")
            }
        }
    }
}