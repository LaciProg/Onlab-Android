package hu.bme.aut.examappbackend.dto

import kotlinx.serialization.Serializable

@Serializable
sealed interface Question{
    val typeOrdinal: Int
}