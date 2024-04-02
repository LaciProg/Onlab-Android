package hu.bme.aut.examappbackend.db.model

import org.jetbrains.exposed.dao.id.UUIDTable

import org.jetbrains.exposed.sql.ReferenceOption

object ExamDB : UUIDTable() {
    val name = text("name")
    val questionList = text("questionList")
    val topicId = reference(name = "topicId", refColumn = TopicDB.id, onDelete = ReferenceOption.RESTRICT, onUpdate = ReferenceOption.CASCADE)
}