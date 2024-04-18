package hu.bme.aut.examappbackend.dto

import hu.bme.aut.examappbackend.serializers.MultipleChoiceSerializer
import hu.bme.aut.examappbackend.serializers.TrueFalseQuestionSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExamDto(
    val uuid: String = "",
    val name: String,
    @SerialName("questionList")
    val questionList: String, //List<Question?>
    val topicId: String
)