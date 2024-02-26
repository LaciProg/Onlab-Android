package hu.bme.aut.android.examapp.data.repositories.offline

import hu.bme.aut.android.examapp.data.repositories.inrefaces.TypeRepository
import hu.bme.aut.android.examapp.data.room.dao.TypeDao
import hu.bme.aut.android.examapp.data.room.dto.TypeDto
import kotlinx.coroutines.flow.Flow

class OfflineTypeRepository(private val typeDao: TypeDao) : TypeRepository {
    override suspend fun insertType(type: TypeDto) = typeDao.insertType(type)

    override suspend fun updateType(type: TypeDto) = typeDao.updateType(type)

    override suspend fun deleteType(type: TypeDto) = typeDao.deleteType(type)

    override fun getAllTypes(): Flow<List<TypeDto>> = typeDao.getAllTypes()

    override fun getTypeById(id: Int): Flow<TypeDto>  = typeDao.getTypeById(id)

    override fun getAllTypeType(): Flow<List<String>> = typeDao.getAllTypeType()
}