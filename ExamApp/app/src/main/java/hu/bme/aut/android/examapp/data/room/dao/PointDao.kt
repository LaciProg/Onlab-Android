package hu.bme.aut.android.examapp.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hu.bme.aut.android.examapp.data.room.dto.PointDto
import kotlinx.coroutines.flow.Flow

@Dao
interface PointDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPoint(point: PointDto)

    @Update
    suspend fun updatePoint(point: PointDto)

    @Delete
    suspend fun deletePoint(point: PointDto)

    @Query("SELECT * FROM point")
    fun getAllPoints(): Flow<List<PointDto>>

    @Query("SELECT * FROM point WHERE id = :id")
    fun getPointById(id: Int): Flow<PointDto>

    @Query("SELECT type FROM point ORDER BY type ASC")
    fun getAllPointType(): Flow<List<String>>
}