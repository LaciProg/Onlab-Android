package hu.bme.aut.examappbackend.serializers

import hu.bme.aut.examappbackend.dto.MultipleChoiceQuestionDto
import hu.bme.aut.examappbackend.dto.TrueFalseQuestionDto
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object MultipleChoiceSerializer: KSerializer<MultipleChoiceQuestionDto> {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("MultipleChoiceQuestion", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): MultipleChoiceQuestionDto {
        val string = decoder.decodeString()
        val parts = string.split("¤")
        return MultipleChoiceQuestionDto(
            uuid = parts[0],
            question = parts[1],
            answers = listOf(""),
            correctAnswersList = listOf(""),
            point = parts[4],
            topic = parts[5],
            type = parts[6],
        )
    }

    override fun serialize(encoder: Encoder, value: MultipleChoiceQuestionDto) {
        encoder.encodeString("${value.uuid}¤${value.question}¤${value.answers}¤${value.correctAnswersList}¤${value.point}¤${value.topic}¤${value.type}")
    }

}