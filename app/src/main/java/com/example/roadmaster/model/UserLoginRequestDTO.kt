package com.example.roadmaster.model;

import kotlinx.serialization.Serializable;

@Serializable
data class UserLoginRequestDTO (
    val username: String,
    val email: String,
    val password: String
)
