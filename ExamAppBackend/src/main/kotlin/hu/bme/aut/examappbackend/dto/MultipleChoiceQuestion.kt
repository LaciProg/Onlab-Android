package hu.bme.aut.examappbackend.dto

import enums.Type
import kotlinx.serialization.Serializable

@Serializable
data class MultipleChoiceQuestionDto(
    val uuid: String,
    val question: String,
    val answers: String,
    val correctAnswersList: String,
    val point: String,
    val topic: String,
    val type: String
): Question{
    override val typeOrdinal: Int
        get() = Type.multipleChoiceQuestion.ordinal
}