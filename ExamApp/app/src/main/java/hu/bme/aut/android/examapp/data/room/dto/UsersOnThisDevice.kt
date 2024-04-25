package hu.bme.aut.android.examapp.data.room.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "users_on_this_device",
    indices = [androidx.room.Index(value = ["uuid"], unique = true)]
)
data class UsersOnThisDevice(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int,
    @ColumnInfo(name = "uuid") var uuid: String,
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "password") var password: String
)
