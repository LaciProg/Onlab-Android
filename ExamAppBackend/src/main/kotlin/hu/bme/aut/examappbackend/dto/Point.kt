package hu.bme.aut.examappbackend.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PointDto(
    val uuid: String = "",
    val point: Double,
    val type: String,
    @SerialName("goodAnswer")
    val goodAnswer: Double,
    @SerialName("badAnswer")
    val badAnswer: Double,
)