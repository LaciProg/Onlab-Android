package hu.bme.aut.examappbackend.db.facade

import hu.bme.aut.examappbackend.db.DatabaseFactory.dbQuery
import hu.bme.aut.examappbackend.db.model.PointDB
import hu.bme.aut.examappbackend.dto.NameDto
import hu.bme.aut.examappbackend.dto.PointDto
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class PointFacadeExposed : PointFacade {

    private fun resultRowToPoint(row: ResultRow) = PointDto(
         uuid = row[PointDB.id].toString(),
         point = row[PointDB.point],
         type = row[PointDB.type],
         goodAnswer = row[PointDB.goodAnswer],
         badAnswer = row[PointDB.badAnswer]
    )

    override suspend fun getAllPoint(): List<PointDto> = dbQuery {
        PointDB.selectAll().map(::resultRowToPoint)
    }

    override suspend fun getAllPointType(): List<NameDto> = dbQuery {
        PointDB.selectAll().map{ NameDto(
            name = it[PointDB.type],
            uuid = it[PointDB.id].toString()
        ) }
    }

    override suspend fun getPointById(uuid: String): PointDto? = dbQuery {
        PointDB.selectAll().where { PointDB.id eq UUID.fromString(uuid) }
            .map(::resultRowToPoint)
            .singleOrNull()
    }

    override suspend fun insertPoint(pointDto: PointDto): PointDto? = dbQuery {
        val insertStatement = PointDB.insert {
            it[point] = pointDto.point
            it[type] = pointDto.type
            it[goodAnswer] = pointDto.goodAnswer
            it[badAnswer] = pointDto.badAnswer
        }
        return@dbQuery insertStatement.resultedValues?.singleOrNull<ResultRow>()?.let(::resultRowToPoint)
    }

    override suspend fun deletePoint(uuid: String): Boolean = dbQuery {
        PointDB.deleteWhere {PointDB.id eq UUID.fromString(uuid)} > 0
    }

    override suspend fun updatePoint(pointDto: PointDto): Boolean = dbQuery {
        PointDB.update({ PointDB.id eq UUID.fromString(pointDto.uuid) }) {
            it[point] = pointDto.point
            it[type] = pointDto.type
            it[goodAnswer] = pointDto.goodAnswer
            it[badAnswer] = pointDto.badAnswer
        } > 0
    }
}