package hu.bme.aut.examappbackend.dto

import kotlinx.serialization.Serializable

@Serializable
data class StatisticsDto(
    val earnedPoints: Double,
    val percentage: Double
)