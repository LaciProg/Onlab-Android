package hu.bme.aut.examappbackend.db.facade

import hu.bme.aut.examappbackend.db.DatabaseFactory.dbQuery
import hu.bme.aut.examappbackend.db.model.*
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

    override suspend fun getAllPointType(): List<String> = dbQuery {
        PointDB.selectAll().map{ it[PointDB.type]}
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
        if(
            TrueFalseQuestionDB.selectAll().where(TrueFalseQuestionDB.point eq UUID.fromString(uuid))
                .map { it[TrueFalseQuestionDB.id] }.isEmpty()
            &&
            MultipleChoiceQuestionDB.selectAll().where(MultipleChoiceQuestionDB.point eq UUID.fromString(uuid))
                .map { it[MultipleChoiceQuestionDB.id] }.isEmpty()
        ) {
            PointDB.deleteWhere { PointDB.id eq UUID.fromString(uuid) } > 0
        } else false
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