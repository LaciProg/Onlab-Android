package hu.bme.aut.android.examapp.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import hu.bme.aut.android.examapp.data.room.dto.UsersOnThisDevice
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: UsersOnThisDevice)
    @Delete
    fun deleteUser(user: UsersOnThisDevice)
    @Update
    fun updateUser(user: UsersOnThisDevice)
    @Query("SELECT * FROM users_on_this_device")
    fun getAllUsers(): Flow<List<UsersOnThisDevice>>

    @Query("SELECT * FROM users_on_this_device WHERE uuid = :uuid")
    fun getUserByUuid(uuid: String): UsersOnThisDevice?
}