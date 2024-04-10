package hu.bme.aut.examappbackend.db.facade

import hu.bme.aut.examappbackend.db.DatabaseFactory.dbQuery
import hu.bme.aut.examappbackend.db.model.TrueFalseQuestionDB
import hu.bme.aut.examappbackend.dto.TrueFalseQuestionDto
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.UUID

class TrueFalseQuestionFacadeExposed : TrueFalseQuestionFacade {

    private fun resultRowToTrueFalseQuestion(row: ResultRow) = TrueFalseQuestionDto(
        uuid = row[TrueFalseQuestionDB.id].toString(),
        question = row[TrueFalseQuestionDB.question],
        correctAnswer = row[TrueFalseQuestionDB.correctAnswer],
        point = row[TrueFalseQuestionDB.point].toString(),
        topic = row[TrueFalseQuestionDB.topic].toString(),
        type = row[TrueFalseQuestionDB.type].toString(),
    )

    override suspend fun getAllTrueFalseQuestion(): List<TrueFalseQuestionDto> = dbQuery {
        TrueFalseQuestionDB.selectAll().map(::resultRowToTrueFalseQuestion)
    }

    override suspend fun getAllTrueFalseQuestionQuestion(): List<String> = dbQuery {
        TrueFalseQuestionDB.selectAll().map{ it[TrueFalseQuestionDB.question] }
    }

    override suspend fun getTrueFalseQuestionById(uuid: String): TrueFalseQuestionDto? = dbQuery {
        TrueFalseQuestionDB.selectAll().where(TrueFalseQuestionDB.id eq UUID.fromString(uuid))
            .map(::resultRowToTrueFalseQuestion)
            .singleOrNull()
    }

    override suspend fun deleteTrueFalseQuestion(uuid: String): Boolean = dbQuery {
        TrueFalseQuestionDB.deleteWhere { TrueFalseQuestionDB.id eq UUID.fromString(uuid) } > 0
    }

    override suspend fun insertTrueFalseQuestion(trueFalseQuestion: TrueFalseQuestionDto): TrueFalseQuestionDto? = dbQuery {
        val insertStatement = TrueFalseQuestionDB.insert {
            it[question] = trueFalseQuestion.question
            it[correctAnswer] = trueFalseQuestion.correctAnswer
            it[point] = UUID.fromString(trueFalseQuestion.point)
            it[topic] = UUID.fromString(trueFalseQuestion.topic)
            it[type] = UUID.fromString(trueFalseQuestion.type)
        }
        return@dbQuery insertStatement.resultedValues?.singleOrNull<ResultRow>()?.let(::resultRowToTrueFalseQuestion)

    }

    override suspend fun updateTrueFalseQuestion(trueFalseQuestion: TrueFalseQuestionDto): Boolean = dbQuery {
        TrueFalseQuestionDB.update({ TrueFalseQuestionDB.id eq UUID.fromString(trueFalseQuestion.uuid) }) {
            it[question] = trueFalseQuestion.question
            it[correctAnswer] = trueFalseQuestion.correctAnswer
            it[point] = UUID.fromString(trueFalseQuestion.point)
            it[topic] = UUID.fromString(trueFalseQuestion.topic)
            it[type] = UUID.fromString(trueFalseQuestion.type)
        } > 0
    }
}