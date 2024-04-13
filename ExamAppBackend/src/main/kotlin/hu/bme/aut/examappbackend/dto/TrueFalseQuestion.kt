package hu.bme.aut.examappbackend.dto

import enums.Type
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrueFalseQuestionDto(
    val uuid: String = "",
    val question: String,
    val correctAnswer: Boolean,
    val point: String,
    val topic: String,
    @SerialName("type_id")
    val type: String
) : Question {
    override val typeOrdinal: Int
        get() = Type.trueFalseQuestion.ordinal
}