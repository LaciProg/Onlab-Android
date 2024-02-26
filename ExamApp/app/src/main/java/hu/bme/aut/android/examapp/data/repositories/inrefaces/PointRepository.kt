package hu.bme.aut.android.examapp.data.repositories.inrefaces

import hu.bme.aut.android.examapp.data.room.dto.PointDto
import kotlinx.coroutines.flow.Flow

interface PointRepository {
    suspend fun insertPoint(point: PointDto)
    suspend fun updatePoint(point: PointDto)
    suspend fun deletePoint(point: PointDto)
    fun getAllPoints(): Flow<List<PointDto>>
    fun getPointById(id: Int): Flow<PointDto>
    fun getAllPointType(): Flow<List<String>>
}