package com.example.roadmaster.model

import kotlinx.serialization.Serializable

@Serializable
data class ResultDTO(
    val user_id: String,
    val category: String,
    val nr_correct: Int,
    val nr_wrong: Int,
    val percentage_correct : Int,
    val result_test: String,
)
