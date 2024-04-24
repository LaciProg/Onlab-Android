package hu.bme.aut.examappbackend.db.facade

import hu.bme.aut.examappbackend.db.DatabaseFactory.dbQuery
import hu.bme.aut.examappbackend.db.model.TypeDB
import hu.bme.aut.examappbackend.dto.NameDto
import hu.bme.aut.examappbackend.dto.TypeDto
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.UUID

class TypeFacadeExposed : TypeFacade {

    private fun resultRowToType(row: ResultRow) = TypeDto(
        uuid = row[TypeDB.id].toString(),
        type = row[TypeDB.type]
    )

    override suspend fun getAllType(): List<TypeDto> = dbQuery {
        TypeDB.selectAll().map(::resultRowToType)
    }

    override suspend fun getAllTypeType(): List<NameDto> = dbQuery {
        TypeDB.selectAll().map { NameDto(
            name = it[TypeDB.type],
            uuid = it[TypeDB.id].toString()
        ) }
    }

    override suspend fun getTypeById(uuid: String): TypeDto? = dbQuery {
        TypeDB.selectAll().where { TypeDB.id eq UUID.fromString(uuid) }
            .map(::resultRowToType)
            .singleOrNull()
    }

    override suspend fun getTypeByType(type: String): TypeDto? = dbQuery {
        TypeDB.selectAll().where { TypeDB.type eq type }
            .map(::resultRowToType)
            .singleOrNull()
    }

    override suspend fun updateType(type: TypeDto): Boolean = dbQuery {
        TypeDB.update({ TypeDB.id eq UUID.fromString(type.uuid) }){
            it[this.type] = type.type
        } > 0
    }

    override suspend fun deleteType(uuid: String): Boolean = dbQuery {
        TypeDB.deleteWhere { TypeDB.id eq UUID.fromString(uuid) } > 0
    }

    override suspend fun insertType(type: TypeDto): TypeDto? = dbQuery {
        val insertStatement = TypeDB.insert {
            it[this.type] = type.type
        }
        return@dbQuery insertStatement.resultedValues?.singleOrNull<ResultRow>()?.let(::resultRowToType)
    }
}