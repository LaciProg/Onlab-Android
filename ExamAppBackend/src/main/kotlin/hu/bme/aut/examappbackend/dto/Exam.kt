package hu.bme.aut.examappbackend.dto

import kotlinx.serialization.Serializable

@Serializable
data class ExamDto(
    val uuid: String,
    val name: String,
    val questionList: List<Question?>,
    val topicId: String
)