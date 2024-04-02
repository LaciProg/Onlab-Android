package hu.bme.aut.examappbackend.dto

import kotlinx.serialization.Serializable

@Serializable
data class PointDto(
    val uuid: String,
    val point: Double,
    val type: String,
    val goodAnswer: Double,
    val badAnswer: Double,
)