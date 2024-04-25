package hu.bme.aut.android.examapp.ui.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.R
import hu.bme.aut.android.examapp.api.ExamAppApi
import hu.bme.aut.android.examapp.data.auth.AuthService
import hu.bme.aut.android.examapp.data.repositories.inrefaces.UserRepository
import hu.bme.aut.android.examapp.ui.model.UiText
import hu.bme.aut.android.examapp.ui.model.toUiText
import hu.bme.aut.android.examapp.ui.viewmodel.auth.usecases.IsEmailValidUseCase
import hu.bme.aut.android.examapp.util.UiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginUserViewModel(
    private val authService: AuthService,
    private val isEmailValid: IsEmailValidUseCase,
    private val userRepository: UserRepository
): ViewModel() {

    private val _state = MutableStateFlow(LoginUserState())
    val state = _state.asStateFlow()

    private val email get() = state.value.email

    private val password get() = state.value.password

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val users: StateFlow<List<String>> =
        userRepository.getAllUsers().map { userDtos -> userDtos.map { it.name } }
            .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            listOf("")
        )


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun onEvent(event: LoginUserEvent) {
        when(event) {
            is LoginUserEvent.EmailChanged -> {
                val newEmail = event.email.trim()
                _state.update { it.copy(email = newEmail) }
            }
            is LoginUserEvent.PasswordChanged -> {
                val newPassword = event.password.trim()
                _state.update { it.copy(password = newPassword) }
            }
            LoginUserEvent.PasswordVisibilityChanged -> {
                _state.update { it.copy(passwordVisibility = !state.value.passwordVisibility) }
            }
            LoginUserEvent.SignIn -> {
                onSignIn()
            }
        }
    }

    fun hasUser(){
        viewModelScope.launch {
            if(authService.hasUser){
                val user = authService.getCurrentUser()
                if (user != null) {
                    try{
                        ExamAppApi.authenticate(user)
                    } catch (e: Exception){
                        _uiEvent.send(UiEvent.Failure(e.toUiText()))
                    }
                }
                _uiEvent.send(UiEvent.Success)
            }
        }
    }

    private fun onSignIn() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (!isEmailValid(email)) {
                    _uiEvent.send(
                        UiEvent.Failure(UiText.StringResource(R.string.some_error_message))
                    )
                } else {
                    if (password.isBlank()) {
                        _uiEvent.send(
                            UiEvent.Failure(UiText.StringResource(R.string.some_error_message))
                        )
                    } else {
                        authService.authenticate(email,password)
                        authService.authenticateInApi(email,password)
                        _uiEvent.send(UiEvent.Success)
                    }
                }
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
            }
        }
    }
}


data class LoginUserState(
    val email: String = "",
    val password: String = "",
    val passwordVisibility: Boolean = false
)

sealed class LoginUserEvent {
    data class EmailChanged(val email: String): LoginUserEvent()
    data class PasswordChanged(val password: String): LoginUserEvent()
    data object PasswordVisibilityChanged: LoginUserEvent()
    data object SignIn: LoginUserEvent()
}