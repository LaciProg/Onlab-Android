package hu.bme.aut.examappbackend.db.model

import org.jetbrains.exposed.dao.id.UUIDTable

object TypeDB : UUIDTable() {
    val type = text("type")
}