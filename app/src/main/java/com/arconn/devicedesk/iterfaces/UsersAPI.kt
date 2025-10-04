package com.arconn.devicedesk.iterfaces

import com.arconn.devicedesk.model.CreateUser
import com.arconn.devicedesk.model.UpdateUser
import com.arconn.devicedesk.model.UsersModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsersAPI {

    @GET("users")
    suspend fun getUsers(): List<UsersModel>

    @POST("users")
    suspend fun saveUser(@Body body: CreateUser) : UsersModel

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Int) : Response<Unit>

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body body: UpdateUser) : UsersModel
}
