package com.example.roadmaster.model

import kotlinx.serialization.Serializable

@Serializable
data class UserRegisterRequestDTO(
    val username: String,
    val email: String,
    val password: String
)
