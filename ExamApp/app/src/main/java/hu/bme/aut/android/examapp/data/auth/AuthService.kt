package hu.bme.aut.android.examapp.data.auth

import hu.bme.aut.android.examapp.api.dto.UserDto
import hu.bme.aut.android.examapp.api.dto.UserFireBase
import kotlinx.coroutines.flow.Flow

interface AuthService {
    val currentUserId: String?

    val hasUser: Boolean

    val currentUser: Flow<UserFireBase?>

    suspend fun signUp(
        email: String, password: String,
    )

    suspend fun registerInApi(
        email: String, password: String,
    )

    suspend fun authenticate(
        email: String,
        password: String
    )

    suspend fun authenticateInApi(
        email: String,
        password: String
    )

    suspend fun sendRecoveryEmail(email: String)

    suspend fun deleteAccount()

    suspend fun signOut()

    suspend fun getCurrentUser(): UserDto?
}