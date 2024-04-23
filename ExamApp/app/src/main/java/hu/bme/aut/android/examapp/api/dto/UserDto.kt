package hu.bme.aut.android.examapp.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val uuid: String = "",
    val name: String,
    val password: String
)