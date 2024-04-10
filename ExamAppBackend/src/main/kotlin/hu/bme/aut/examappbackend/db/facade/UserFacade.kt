package hu.bme.aut.examappbackend.db.facade

import hu.bme.aut.examappbackend.dto.UserDto

interface UserFacade {
    suspend fun getAllUser(): List<UserDto>
    suspend fun getUserById(uuid: String): UserDto?
    suspend fun deleteUser(user: String): Boolean
    suspend fun updateUser(user: UserDto): Boolean
    suspend fun insertUser(user: UserDto): UserDto?
    suspend fun getUserByName(name: String): UserDto?
}