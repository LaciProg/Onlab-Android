package hu.bme.aut.examappbackend.db.facade

import enums.Type
import hu.bme.aut.examappbackend.db.DatabaseFactory.dbQuery
import hu.bme.aut.examappbackend.db.model.ExamDB
import hu.bme.aut.examappbackend.dto.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class ExamFacadeExposed : ExamFacade{

    private fun resultRowToExam(row: ResultRow) = ExamDto(
        uuid = row[ExamDB.id].toString(),
        name = row[ExamDB.name],
        questionList = row[ExamDB.questionList] /*row[ExamDB.questionList].split("¤").map { it.toQuestion() }*/,
        topicId = row[ExamDB.topicId].toString()
    )

    override suspend fun getAllExam(): List<ExamDto> = dbQuery {
        ExamDB.selectAll().map(::resultRowToExam)
    }

    override suspend fun getAllExamNames(): List<NameDto> = dbQuery {
        ExamDB.selectAll().map{NameDto(
            name = it[ExamDB.name],
            uuid = it[ExamDB.id].toString()
        ) }
    }

    override suspend fun getAllQuestionString(exam: String): String? = dbQuery {
        ExamDB.selectAll().where { ExamDB.name eq exam }
            .map { it[ExamDB.questionList] }
            .singleOrNull()
    }

    override suspend fun getAllQuestionStringById(uuid: String): String? = dbQuery {
        ExamDB.selectAll().where { ExamDB.id eq UUID.fromString(uuid) }
            .map { it[ExamDB.questionList] }
            .singleOrNull()
    }

    override suspend fun getAllQuestion(): List<Question> {
        val questions: MutableList<Question> = mutableListOf()
        val examIds = FacadeExposed.examDao.getAllExam().map { it.uuid }
        val questionStrings: HashSet<String> = hashSetOf()
        examIds.forEach { FacadeExposed.examDao.getAllQuestionStringById(it)?.let { it1 -> questionStrings.add(it1) } }
        val usedQuestions: HashSet<String> = hashSetOf()
        questionStrings.forEach { usedQuestions.addAll(it.split("#")) }
        usedQuestions.forEach {
            when(it.substringBefore("~").toInt()){
                Type.trueFalseQuestion.ordinal -> {
                    val q = FacadeExposed.trueFalseQuestionDao.getTrueFalseQuestionById(it.substringAfter("~"))
                    if(q != null){ questions.add(q) }
                    println(q)
                }
                Type.multipleChoiceQuestion.ordinal -> {
                    val q = FacadeExposed.multipleChoiceQuestionDao.getMultipleChoiceQuestionById(it.substringAfter("~"))
                    if(q != null){ questions.add(q) }
                    println(q)
                }
            }
        }
        return questions
    }

    override suspend fun getAllQuestionId(): List<String> {
        val questions: MutableList<String> = mutableListOf()
        val examIds = FacadeExposed.examDao.getAllExam().map { it.uuid }
        val questionStrings: HashSet<String> = hashSetOf()
        examIds.forEach { FacadeExposed.examDao.getAllQuestionStringById(it)?.let { it1 -> questionStrings.add(it1) } }
        val usedQuestions: MutableList<String> = mutableListOf()
        questionStrings.forEach { usedQuestions.addAll(it.split("#")) }

        usedQuestions.forEach {
            when(it.substringBefore("~").toInt()){
                Type.trueFalseQuestion.ordinal -> {
                    val q = FacadeExposed.trueFalseQuestionDao.getTrueFalseQuestionById(it.substringAfter("~"))
                    if(q != null){ questions.add(q.uuid) }
                }
                Type.multipleChoiceQuestion.ordinal -> {
                    val q = FacadeExposed.multipleChoiceQuestionDao.getMultipleChoiceQuestionById(it.substringAfter("~"))
                    if(q != null){ questions.add(q.uuid) }
                }
            }
        }
        return questions
    }

    override suspend fun getExamById(uuid: String): ExamDto? = dbQuery {
        ExamDB.selectAll().where { ExamDB.id eq UUID.fromString(uuid) }
            .map(::resultRowToExam)
            .singleOrNull()
    }

    override suspend fun getExamByName(exam: String): ExamDto? = dbQuery {
        ExamDB.selectAll().where { ExamDB.name eq exam }
            .map(::resultRowToExam)
            .singleOrNull()
    }

    override suspend fun deleteExam(uuid: String): Boolean = dbQuery {
        ExamDB.deleteWhere { ExamDB.id eq UUID.fromString(uuid) } > 0
    }

    override suspend fun insertExam(exam: ExamDto): ExamDto? = dbQuery{
        val insertStatement = ExamDB.insert {
            it[name] = exam.name
            it[questionList] = exam.questionList//.joinToString("¤") { question -> question?.toQuestionString() ?: "" }
            it[topicId] = UUID.fromString(exam.topicId)
        }
        return@dbQuery insertStatement.resultedValues?.singleOrNull<ResultRow>()?.let(::resultRowToExam)
    }


    override suspend fun updateExam(exam: ExamDto): Boolean = dbQuery {
        ExamDB.update( {ExamDB.id eq UUID.fromString(exam.uuid)} ){
            it[name] = exam.name
            it[questionList] = exam.questionList//.joinToString("¤") { question -> question?.toQuestionString() ?: "" }
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
}