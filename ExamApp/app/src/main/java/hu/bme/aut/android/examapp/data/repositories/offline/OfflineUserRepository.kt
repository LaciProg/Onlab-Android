package hu.bme.aut.android.examapp.data.repositories.offline

import hu.bme.aut.android.examapp.api.dto.UserDto
import hu.bme.aut.android.examapp.data.repositories.inrefaces.UserRepository
import hu.bme.aut.android.examapp.data.room.dao.UserDao
import hu.bme.aut.android.examapp.data.room.dto.UsersOnThisDevice
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineUserRepository @Inject constructor(private val userDao: UserDao) : UserRepository {
    override suspend fun insertUser(user: UserDto) = userDao.insertUser(user.toUsersOnThisDevice())
    override suspend fun updateUser(user: UserDto) = userDao.updateUser(user.toUsersOnThisDevice())
    override suspend fun deleteUser(user: UserDto) = userDao.deleteUser(user.toUsersOnThisDevice())
    override fun getAllUsers() = userDao.getAllUsers().map { it.map{ it.toUserDto() } }
    override fun getUserByUid(uuid: String) = userDao.getUserByUuid(uuid)?.toUserDto()
}

private fun UserDto.toUsersOnThisDevice(): UsersOnThisDevice {
    return UsersOnThisDevice(
        uuid = uuid,
        email = name,
        password = password,
        id = 0
    )
}

private fun UsersOnThisDevice.toUserDto(): UserDto {
    return UserDto(
        uuid = uuid,
        name = email,
        password = password
    )
}