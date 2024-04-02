package hu.bme.aut.examappbackend.db.model

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object MultipleChoiceQuestionDB : UUIDTable() {
    val question = text("question")
    val answers = text("answers")
    val correctAnswersList = text("correctAnswersList")
    val point = reference(name = "point", refColumn = PointDB.id, onDelete = ReferenceOption.SET_NULL, onUpdate = ReferenceOption.CASCADE)
    val topic = reference(name = "topic", refColumn = TopicDB.id, onDelete = ReferenceOption.SET_NULL, onUpdate = ReferenceOption.CASCADE)
    val type = reference(name = "type", refColumn = TypeDB.id, onDelete = ReferenceOption.SET_NULL, onUpdate = ReferenceOption.CASCADE)
}