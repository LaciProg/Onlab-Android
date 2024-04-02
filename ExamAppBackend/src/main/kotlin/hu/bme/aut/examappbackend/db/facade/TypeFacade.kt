package hu.bme.aut.examappbackend.db.facade

import hu.bme.aut.examappbackend.dto.TypeDto

interface TypeFacade {
    suspend fun getAllType() : List<TypeDto>
    suspend fun updateType(type: TypeDto) : TypeDto?
    suspend fun deleteType(type: TypeDto) : Boolean
    suspend fun insertType(type: TypeDto) : TypeDto?
    suspend fun getTypeById(uuid: String) : TypeDto?
    suspend fun getAllTypeType() : List<String>
}