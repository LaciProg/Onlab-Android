package hu.bme.aut.examappbackend.serializers

import hu.bme.aut.examappbackend.dto.TrueFalseQuestionDto
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object TrueFalseQuestionSerializer : KSerializer<TrueFalseQuestionDto> {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("TrueFalseQuestion", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): TrueFalseQuestionDto {
        val string = decoder.decodeString()
        val parts = string.split("¤")
        return TrueFalseQuestionDto(
            uuid = parts[0],
            question = parts[1],
            correctAnswer = parts[2].toBoolean(),
            point = parts[3],
            topic = parts[4],
            type = parts[5],
        )
    }

    override fun serialize(encoder: Encoder, value: TrueFalseQuestionDto) {
        encoder.encodeString("${value.uuid}¤${value.question}¤${value.correctAnswer}¤${value.point}¤${value.topic}¤${value.type}")
    }

}