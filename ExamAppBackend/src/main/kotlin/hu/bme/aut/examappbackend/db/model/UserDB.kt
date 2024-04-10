package hu.bme.aut.examappbackend.db.model

import org.jetbrains.exposed.dao.id.UUIDTable

object UserDB : UUIDTable() {
    val name = text("name")
    val password = text("password")
}