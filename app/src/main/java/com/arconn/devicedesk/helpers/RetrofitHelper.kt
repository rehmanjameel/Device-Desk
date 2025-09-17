package com.arconn.devicedesk.helpers

import com.arconn.devicedesk.iterfaces.UsersAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    const val baseUrl = "http://192.168.0.222:8080/"

    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }

    val apiService: UsersAPI = getInstance().create(UsersAPI::class.java)
}