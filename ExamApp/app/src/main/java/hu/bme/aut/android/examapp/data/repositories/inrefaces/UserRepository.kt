package hu.bme.aut.android.examapp.data.repositories.inrefaces

import hu.bme.aut.android.examapp.api.dto.UserDto
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun insertUser(user: UserDto)
    suspend fun updateUser(user: UserDto)
    suspend fun deleteUser(user: UserDto)
    fun getAllUsers(): Flow<List<UserDto>>
    fun getUserByUid(uuid: String): UserDto?
}