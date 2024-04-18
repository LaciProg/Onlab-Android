package hu.bme.aut.examappbackend.dto

import kotlinx.serialization.Serializable

@Serializable
data class NameDto(
    val name: String,
    val uuid: String
)