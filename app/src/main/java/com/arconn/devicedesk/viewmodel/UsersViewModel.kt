package com.arconn.devicedesk.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arconn.devicedesk.helpers.Resource
import com.arconn.devicedesk.model.CreateUser
import com.arconn.devicedesk.model.UpdateUser
import com.arconn.devicedesk.model.UsersModel
import com.arconn.devicedesk.repository.UserRepository
import kotlinx.coroutines.launch
import okhttp3.Response

class UsersViewModel(private val repository: UserRepository) : ViewModel() {

    private val _users = MutableLiveData<Resource<List<UsersModel>>>()
    val users: LiveData<Resource<List<UsersModel>>> = _users

    private val _userCreation = MutableLiveData<Resource<UsersModel>>()
    val userCreation: LiveData<Resource<UsersModel>> = _userCreation

    private val _userDeletion = MutableLiveData<Resource<UsersModel>>()
    val userDeletion: LiveData<Resource<UsersModel>> = _userDeletion

    private val _userUpdate = MutableLiveData<Resource<UsersModel>>()
    val userUpdate: LiveData<Resource<UsersModel>> = _userUpdate


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

    fun deleteUser(id: Int) {
        viewModelScope.launch {
            _userDeletion.value = Resource.Loading()
            try {
                val response = repository.deleteUser(id)
                _userDeletion.value = Resource.Success(response)
            } catch (e: Exception) {
                _userDeletion.value = Resource.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun updateUser(id: Int, userName: String, email: String) {
        viewModelScope.launch {
            _userUpdate.value = Resource.Loading()
            try {
                val requestUser = UpdateUser(userName, email)
                val response = repository.updateUser(id, requestUser)
                _userUpdate.value = Resource.Success(response)
            } catch (e: Exception) {
                _userUpdate.value = Resource.Error(e.message ?: "Unknown Error")
            }
        }
    }
}