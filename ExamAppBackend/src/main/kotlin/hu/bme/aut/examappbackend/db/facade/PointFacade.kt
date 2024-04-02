package hu.bme.aut.examappbackend.db.facade

import hu.bme.aut.examappbackend.dto.PointDto

interface PointFacade {
    suspend fun getAllPoint() : List<PointDto>
    suspend fun getPointById(uuid: String) : PointDto?
    suspend fun insertPoint(pointDto: PointDto) : PointDto?
    suspend fun deletePoint(pointDto: PointDto) : Boolean
    suspend fun updatePoint(pointDto: PointDto) : PointDto?
    suspend fun getAllPointType() : List<String>
}