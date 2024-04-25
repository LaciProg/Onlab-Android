package hu.bme.aut.android.examapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.data.auth.AuthService
import kotlinx.coroutines.launch

class MainScreenViewModel(
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