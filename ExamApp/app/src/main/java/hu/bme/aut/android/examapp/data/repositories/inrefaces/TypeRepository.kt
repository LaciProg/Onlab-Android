package hu.bme.aut.android.examapp.data.repositories.inrefaces

import hu.bme.aut.android.examapp.data.room.dto.TypeDto
import kotlinx.coroutines.flow.Flow

interface TypeRepository {
    suspend fun insertType(type: TypeDto)
    suspend fun updateType(type: TypeDto)
    suspend fun deleteType(type: TypeDto)
    fun getAllTypes(): Flow<List<TypeDto>>
    fun getTypeById(id: Int): Flow<TypeDto>
    fun getAllTypeType(): Flow<List<String>>
}