package hu.bme.aut.examappbackend.db.model

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object PointDB : UUIDTable() {
    val point = double("point")
    val type = text("type")
    val goodAnswer = double("goodAnswer")
    val badAnswer = double("badAnswer")
}