package hu.bme.aut.examappbackend.db.model

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object TopicDB : UUIDTable() {
    val topic = text("topic")
    val description = text("description")
    val parentTopic = reference(name = "parentTopic", refColumn = TopicDB.id, onDelete = ReferenceOption.SET_NULL, onUpdate = ReferenceOption.CASCADE)
}