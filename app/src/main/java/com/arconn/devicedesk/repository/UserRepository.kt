package com.arconn.devicedesk.repository

import com.arconn.devicedesk.iterfaces.UsersAPI
import com.arconn.devicedesk.model.CreateUser
import com.arconn.devicedesk.model.UpdateUser
import com.arconn.devicedesk.model.UsersModel

class UserRepository(private val usersAPI: UsersAPI) {

    suspend fun fetchUsers(): List<UsersModel> {
        return usersAPI.getUsers()
    }

    suspend fun createUser(request: CreateUser) : UsersModel {
        return usersAPI.saveUser(request)
    }

    suspend fun deleteUser(id: Int) : UsersModel {
        return usersAPI.deleteUser(id)
    }

    suspend fun updateUser(id: Int, request: UpdateUser) : UsersModel {
        return usersAPI.updateUser(id, request)
    }
}
