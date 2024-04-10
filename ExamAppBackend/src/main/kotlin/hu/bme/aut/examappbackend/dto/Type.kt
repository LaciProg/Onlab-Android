package hu.bme.aut.examappbackend.dto

import kotlinx.serialization.Serializable

@Serializable
data class TypeDto(
    val uuid: String = "",
    val type: String
)