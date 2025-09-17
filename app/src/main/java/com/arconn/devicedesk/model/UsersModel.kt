package com.arconn.devicedesk.model

data class UsersModel(
    val id: Int,
    val name: String,
    val email: String,
    val password: String
)

data class CreateUser(
    val name: String,
    val email: String,
    val password: String
)