package hu.bme.aut.android.examapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.examapp.data.auth.AuthService
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val authService: AuthService,
) : ViewModel() {

    val hasUser: Boolean
        get() = authService.hasUser

    val currentUserId: String?
        get() = authService.currentUserId

    fun signOut() {
        viewModelScope.launch {
            authService.signOut()
        }
    }

}