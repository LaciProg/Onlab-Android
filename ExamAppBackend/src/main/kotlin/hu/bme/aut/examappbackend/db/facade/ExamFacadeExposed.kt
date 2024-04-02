package hu.bme.aut.examappbackend.db.facade

import enums.Type
import hu.bme.aut.examappbackend.db.DatabaseFactory.dbQuery
import hu.bme.aut.examappbackend.db.model.ExamDB
import hu.bme.aut.examappbackend.dto.ExamDto
import hu.bme.aut.examappbackend.dto.MultipleChoiceQuestionDto
import hu.bme.aut.examappbackend.dto.Question
import hu.bme.aut.examappbackend.dto.TrueFalseQuestionDto
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class ExamFacadeExposed : ExamFacade{

    private fun resultRowToExam(row: ResultRow) = ExamDto(
        uuid = row[ExamDB.id].toString(),
        name = row[ExamDB.name],
        questionList = row[ExamDB.questionList].split("¤").map { it.toQuestion() },
        topicId = row[ExamDB.topicId].toString()
    )

    override suspend fun getAllExam(): List<ExamDto> = dbQuery {
        ExamDB.selectAll().map(::resultRowToExam)
    }

    override suspend fun getAllExamNames(): List<String> = dbQuery {
        ExamDB.selectAll().map{it[ExamDB.name]}
    }

    override suspend fun getExamById(uuid: String): ExamDto? = dbQuery {
        ExamDB.select { ExamDB.id eq UUID.fromString(uuid) }
            .map(::resultRowToExam)
            .singleOrNull()
    }

    override suspend fun deleteExam(uuid: String): Boolean = dbQuery {
        ExamDB.deleteWhere { ExamDB.id eq UUID.fromString(uuid) } > 0
    }

    override suspend fun insertExam(exam: ExamDto): ExamDto? = dbQuery{
        val insertStatement = ExamDB.insert {
            it[name] = exam.name
            it[questionList] = exam.questionList.joinToString("¤") { question -> question?.toQuestionString() ?: "" }
            it[topicId] = UUID.fromString(exam.topicId)
        }
        return@dbQuery insertStatement.resultedValues?.singleOrNull<ResultRow>()?.let(::resultRowToExam)
    }


    override suspend fun updateExam(exam: ExamDto): Boolean = dbQuery {
        ExamDB.update( {ExamDB.id eq UUID.fromString(exam.uuid)} ){
            it[name] = exam.name
            it[questionList] = exam.questionList.joinToString("¤") { question -> question?.toQuestionString() ?: "" }
            it[topicId] = UUID.fromString(exam.topicId)
        } > 0
    }

    private fun Question.toQuestionString(): String = when(this){
        is TrueFalseQuestionDto -> "${Type.trueFalseQuestion.name}~${this.uuid}"
        is MultipleChoiceQuestionDto -> "${Type.multipleChoiceQuestion.name}~${this.uuid}"
        else -> throw IllegalArgumentException("Invalid type")
    }

    private fun String.toQuestion(
    ): Question? {
        val question = this.split("~")
        val type = question[0]
        val questionId = question[1]
        return when(type){
            Type.trueFalseQuestion.name ->  runBlocking { FacadeExposed.trueFalseQuestionDao.getTrueFalseQuestionById(questionId) } //FacadeExposed.trueFalseQuestionDao.getTrueFalseQuestionById(questionId)
            Type.multipleChoiceQuestion.name -> runBlocking { FacadeExposed.multipleChoiceQuestionDao.getMultipleChoiceQuestionById(questionId) }//toMultipleChoiceQuestion(questionId)
            else -> throw IllegalArgumentException("Invalid type")
        }

    }


/*
    private fun toTrueFalseQuestion(uuid: String) : TrueFalseQuestionDto?  {
        //var job: Job? = null
        //var question: TrueFalseQuestionDto? = null
        //coroutineScope {
        //    job = launch {

              return runBlocking { FacadeExposed.trueFalseQuestionDao.getTrueFalseQuestionById(uuid) }
        //   }
        //}
        //job?.join()
        //job?.invokeOnCompletion {
        //    if(it == null){
        //         return@invokeOnCompletion question
        //    }
        //}
    }

    private suspend fun toMultipleChoiceQuestion(uuid: String) : MultipleChoiceQuestionDto? = dbQuery {
        var job: Job? = null
        var question: MultipleChoiceQuestionDto? = null
        coroutineScope {
            job = launch {
                question = FacadeExposed.multipleChoiceQuestionDao.getMultipleChoiceQuestionById(uuid)
            }
        }
        job?.join()
        return@dbQuery question
    }*/

}