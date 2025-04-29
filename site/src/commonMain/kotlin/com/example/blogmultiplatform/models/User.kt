package com.example.blogmultiplatform.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val _id:String = "",
    val username:String = "",
    val password:String = ""

)


@Serializable
data class UserWithoutPassword(
    val _id:String = "",
    val username:String = "",
)