package hu.bme.aut.android.examapp.util

import hu.bme.aut.android.examapp.ui.model.UiText

sealed class UiEvent {
    data object Success: UiEvent()

    data class Failure(val message: UiText): UiEvent()
}