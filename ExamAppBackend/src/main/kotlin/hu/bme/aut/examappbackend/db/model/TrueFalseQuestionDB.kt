package hu.bme.aut.examappbackend.db.model

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object TrueFalseQuestionDB : UUIDTable() {
    val question = text("question")
    val correctAnswer = bool("correctAnswers")
    val point = reference(name = "point", refColumn = id, onDelete = ReferenceOption.SET_NULL, onUpdate = ReferenceOption.CASCADE)
    val topic = reference(name = "topic", refColumn = id, onDelete = ReferenceOption.SET_NULL, onUpdate = ReferenceOption.CASCADE)
    val type = reference(name = "type", refColumn = id, onDelete = ReferenceOption.SET_NULL, onUpdate = ReferenceOption.CASCADE)
}