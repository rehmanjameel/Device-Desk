package com.arconn.devicedesk.iterfaces

import com.arconn.devicedesk.model.CreateUser
import com.arconn.devicedesk.model.UsersModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UsersAPI {

    @GET("users")
    suspend fun getUsers(): List<UsersModel>

    @POST("users")
    suspend fun saveUser(@Body body: CreateUser) : UsersModel
}