package com.example.roadmaster.model

import TimestampSerializer
import kotlinx.serialization.Serializable
import java.sql.Timestamp

@Serializable
data class HistoryResponseDTO(
    val category: String,
    val nr_correct: Int,
    val result_test: String,

    @Serializable(with = TimestampSerializer::class)
    val date_test: Timestamp
)
