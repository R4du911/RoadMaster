package com.example.roadmaster.model

import kotlinx.serialization.Serializable

@Serializable
data class UserResetPasswordRequest(
    val email: String?,
    val old_password: String,
    val new_password: String,
    val repeat_new_password: String
)
