package com.example.roadmaster.model

import kotlinx.serialization.Serializable

@Serializable
data class GetQuestionRequestDTO(
    val category: String?
)
