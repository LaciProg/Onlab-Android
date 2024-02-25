package hu.bme.aut.android.examapp.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hu.bme.aut.android.examapp.data.room.dto.TypeDto
import kotlinx.coroutines.flow.Flow

@Dao
interface TypeDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertType(type: TypeDto)

    @Update
    suspend fun updateType(type: TypeDto)

    @Delete
    suspend fun deleteType(type: TypeDto)

    @Query("SELECT * FROM type")
    fun getAllTypes(): Flow<List<TypeDto>>

    @Query("SELECT * FROM type WHERE id = :id")
    fun getTypeById(id: Int): Flow<TypeDto>

    @Query("SELECT type FROM type ORDER BY type ASC")
    fun getAllTypeType(): Flow<List<String>>
}