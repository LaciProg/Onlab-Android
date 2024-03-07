package hu.bme.aut.android.examapp.data.repositories.offline

import hu.bme.aut.android.examapp.data.repositories.inrefaces.PointRepository
import hu.bme.aut.android.examapp.data.room.dao.PointDao
import hu.bme.aut.android.examapp.data.room.dto.PointDto
import kotlinx.coroutines.flow.Flow

class OfflinePointRepository(private val pointDao: PointDao) : PointRepository {
    override suspend fun insertPoint(point: PointDto) = pointDao.insertPoint(point)
    override suspend fun updatePoint(point: PointDto) = pointDao.updatePoint(point)
    override suspend fun deletePoint(point: PointDto) = pointDao.deletePoint(point)
    override fun getAllPoints() = pointDao.getAllPoints()
    override fun getPointById(id: Int) = pointDao.getPointById(id)
    override fun getAllPointType() = pointDao.getAllPointType()
    override fun getAllPointName() = pointDao.getAllPointName()
    override fun getPointByType(type: String): Flow<PointDto> = pointDao.getPointByType(type)
}