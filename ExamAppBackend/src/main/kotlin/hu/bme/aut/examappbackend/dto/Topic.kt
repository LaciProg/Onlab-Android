package hu.bme.aut.examappbackend.dto

import kotlinx.serialization.Serializable

@Serializable
data class TopicDto(
    val uuid: String,
    val topic: String,
    val description: String,
    val parentTopic: Int
)