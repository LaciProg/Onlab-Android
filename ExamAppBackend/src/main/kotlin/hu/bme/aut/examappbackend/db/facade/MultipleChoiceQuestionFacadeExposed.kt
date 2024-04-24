package hu.bme.aut.examappbackend.db.facade

import hu.bme.aut.examappbackend.db.DatabaseFactory.dbQuery
import hu.bme.aut.examappbackend.db.model.MultipleChoiceQuestionDB
import hu.bme.aut.examappbackend.dto.MultipleChoiceQuestionDto
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.UUID

class MultipleChoiceQuestionFacadeExposed : MultipleChoiceQuestionFacade {

    private fun resultRowToMultipleChoiceQuestion(row: ResultRow) = MultipleChoiceQuestionDto(
        uuid = row[MultipleChoiceQuestionDB.id].toString(),
        question = row[MultipleChoiceQuestionDB.question],
        answers = row[MultipleChoiceQuestionDB.answers].split("¤"),
        correctAnswersList = row[MultipleChoiceQuestionDB.correctAnswersList].split("¤"),
        point = row[MultipleChoiceQuestionDB.point].toString(),
        topic = row[MultipleChoiceQuestionDB.topic].toString(),
        type = row[MultipleChoiceQuestionDB.type].toString()
    )

    override suspend fun getAllMultipleChoiceQuestion(): List<MultipleChoiceQuestionDto> = dbQuery  {
        MultipleChoiceQuestionDB.selectAll().map(::resultRowToMultipleChoiceQuestion)
    }
    override suspend fun getAllMultipleChoiceQuestionQuestion(): List<String> = dbQuery {
        MultipleChoiceQuestionDB.selectAll().map { it[MultipleChoiceQuestionDB.question] }
    }

    override suspend fun getMultipleChoiceQuestionById(uuid: String): MultipleChoiceQuestionDto? = dbQuery {
        MultipleChoiceQuestionDB.selectAll().where { MultipleChoiceQuestionDB.id eq UUID.fromString(uuid) }
            .map(::resultRowToMultipleChoiceQuestion)
            .singleOrNull()
    }

    override suspend fun deleteMultipleChoiceQuestion(uuid: String): Boolean = dbQuery {
        if(
            !FacadeExposed.examDao.getAllQuestionId().contains(uuid)
        ) {
            MultipleChoiceQuestionDB.deleteWhere { MultipleChoiceQuestionDB.id eq UUID.fromString(uuid) } > 0
        } else false
    }

    override suspend fun insertMultipleChoiceQuestion(multipleChoiceQuestion: MultipleChoiceQuestionDto): MultipleChoiceQuestionDto? = dbQuery {
        val insertStatement = MultipleChoiceQuestionDB.insert {
            it[question] = multipleChoiceQuestion.question
            it[answers] = multipleChoiceQuestion.answers.joinToString("¤" )
            it[correctAnswersList] = multipleChoiceQuestion.correctAnswersList.joinToString("¤" )
            it[point] = UUID.fromString(multipleChoiceQuestion.point)
            it[topic] = UUID.fromString(multipleChoiceQuestion.topic)
            it[type] = UUID.fromString(multipleChoiceQuestion.type)
        }
        return@dbQuery insertStatement.resultedValues?.singleOrNull<ResultRow>()?.let(::resultRowToMultipleChoiceQuestion)
    }

    override suspend fun updateMultipleChoiceQuestion(multipleChoiceQuestion: MultipleChoiceQuestionDto): Boolean = dbQuery {
        MultipleChoiceQuestionDB.update ( {MultipleChoiceQuestionDB.id eq UUID.fromString(multipleChoiceQuestion.uuid)} ){
            it[question] = multipleChoiceQuestion.question
            it[answers] = multipleChoiceQuestion.answers.joinToString("¤" )
            it[correctAnswersList] = multipleChoiceQuestion.correctAnswersList.joinToString("¤" )
            it[point] = UUID.fromString(multipleChoiceQuestion.point)
            it[topic] = UUID.fromString(multipleChoiceQuestion.topic)
            it[type] = UUID.fromString(multipleChoiceQuestion.type)
        } > 0
    }

}