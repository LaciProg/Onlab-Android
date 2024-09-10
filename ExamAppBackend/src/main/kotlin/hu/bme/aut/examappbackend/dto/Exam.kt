package hu.bme.aut.examappbackend.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExamDto(
    val uuid: String = "",
    val name: String,
    @SerialName("questionList")
    val questionList: String,
    val topicId: String
)