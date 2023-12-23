package com.example.roadmaster.model

import kotlinx.serialization.Serializable

@Serializable
data class Country(
    val id: String,
    val name: String
)
