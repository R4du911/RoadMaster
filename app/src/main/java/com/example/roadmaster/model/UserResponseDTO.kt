package com.example.roadmaster.model

import kotlinx.serialization.Serializable

@Serializable
data class UserResponseDTO(
    val id: String,
    val username: String,
    val email: String,
    val password: String
)
