package hu.bme.aut.examappbackend.db

import enums.Type
import hu.bme.aut.examappbackend.db.facade.FacadeExposed
import hu.bme.aut.examappbackend.db.model.*
import hu.bme.aut.examappbackend.dto.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

object DatabaseFactory {

    fun init() {
        val db = Database.connect(
            url = System.getenv("DB_URL") ?: /*"jdbc:postgresql://DB:5432/examapp",*/ "jdbc:postgresql://152.66.211.35:5432/examapp",
            driver = System.getenv("DB_DRIVER") ?: "org.postgresql.Driver",
            user = System.getenv("DB_USER") ?: "examapp",
            password = System.getenv("DB_PASSWORD") ?: "examapp"
        )
        transaction(db) {
            //SchemaUtils.drop(ExamDB)
            //SchemaUtils.drop(TrueFalseQuestionDB)
            //SchemaUtils.drop(MultipleChoiceQuestionDB)
            //SchemaUtils.drop(TypeDB)
            //SchemaUtils.drop(TopicDB)
            //SchemaUtils.drop(PointDB)
            //SchemaUtils.drop(UserDB)

            SchemaUtils.create(TypeDB)
            SchemaUtils.create(TopicDB)
            SchemaUtils.create(PointDB)
            SchemaUtils.create(TrueFalseQuestionDB)
            SchemaUtils.create(MultipleChoiceQuestionDB)
            SchemaUtils.create(ExamDB)
            SchemaUtils.create(UserDB)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    fun createSampleData() {
        var type1: TypeDto? = null
        var type2: TypeDto? = null
        var point1: PointDto? = null
        var point2: PointDto? = null
        var topic1: TopicDto? = null
        var topic2: TopicDto? = null
        var tf1: TrueFalseQuestionDto? = null
        var tf2: TrueFalseQuestionDto? = null
        var mc1: MultipleChoiceQuestionDto? = null
        var mc2: MultipleChoiceQuestionDto? = null
        runBlocking {
            if(FacadeExposed.typeDao.getAllType().isEmpty()){
                type1 = FacadeExposed.typeDao.insertType(
                    TypeDto(
                        type = Type.trueFalseQuestion.name,
                        uuid = UUID.randomUUID().toString()
                    )
                )
                type2 = FacadeExposed.typeDao.insertType(
                    TypeDto(
                        type = Type.multipleChoiceQuestion.name,
                        uuid = UUID.randomUUID().toString()
                    )
                )
            }
            if(FacadeExposed.pointDao.getAllPoint().isEmpty()){
                point1 = FacadeExposed.pointDao.insertPoint(
                    PointDto(
                        type = "IH",
                        point = 2.0,
                        goodAnswer = 2.0,
                        badAnswer = -2.0
                    )
                )
                point2 = FacadeExposed.pointDao.insertPoint(
                    PointDto(
                        type = "MC",
                        point = 4.0,
                        goodAnswer = 1.0,
                        badAnswer = 0.0
                    )
                )
                FacadeExposed.pointDao.insertPoint(
                    PointDto(
                        type = "IHKiller",
                        point = 4.0,
                        goodAnswer = 4.0,
                        badAnswer = -10.0
                    )
                )
            }
            if(FacadeExposed.topicDao.getAllTopic().isEmpty()){
                topic1 = FacadeExposed.topicDao.insertTopic(
                    TopicDto(
                        topic = "Topic1",
                        description = "Első",
                    )
                )
                topic2 = FacadeExposed.topicDao.insertTopic(
                    TopicDto(
                        topic = "Topic2",
                        description = "Második",
                        parentTopic = topic1?.uuid!!
                    )
                )
            }
            if(FacadeExposed.userDao.getAllUser().isEmpty()){
                FacadeExposed.userDao.insertUser(
                    UserDto(
                        name = "user",
                        password = "xdlol"
                    )
                )
            }
       }
        runBlocking {
            if(FacadeExposed.trueFalseQuestionDao.getAllTrueFalseQuestion().isEmpty()){
                tf1 = FacadeExposed.trueFalseQuestionDao.insertTrueFalseQuestion(
                    TrueFalseQuestionDto(
                        question = "Almaaa????",
                        correctAnswer = true,
                        point = point1?.uuid!!,
                        topic = topic1?.uuid!!,
                        type = type1?.uuid!!
                    )
                )
                tf2 = FacadeExposed.trueFalseQuestionDao.insertTrueFalseQuestion(
                    TrueFalseQuestionDto(
                        question = "YEEEAH????",
                        correctAnswer = true,
                        point = point2?.uuid!!,
                        topic = topic2?.uuid!!,
                        type = type2?.uuid!!
                    )
                )
            }
            if(FacadeExposed.multipleChoiceQuestionDao.getAllMultipleChoiceQuestion().isEmpty()){
                mc1 = FacadeExposed.multipleChoiceQuestionDao.insertMultipleChoiceQuestion(
                    MultipleChoiceQuestionDto(
                        question = "Almaaa????",
                        correctAnswersList = listOf("Alma", "Igen"),
                        answers = listOf("Alma", "Igen", "Nem"),
                        point = point1?.uuid!!,
                        topic = topic1?.uuid!!,
                        type = type1?.uuid!!
                    )
                )
                mc2 = FacadeExposed.multipleChoiceQuestionDao.insertMultipleChoiceQuestion(
                    MultipleChoiceQuestionDto(
                        question = "YEEEAH????",
                        answers = listOf("Alma", "Banán", "Körte", "Barack"),
                        correctAnswersList = listOf("Barack"),
                        point = point2?.uuid!!,
                        topic = topic2?.uuid!!,
                        type = type2?.uuid!!
                    )
                )
            }
        }
        runBlocking {
            if(FacadeExposed.examDao.getAllExam().isEmpty()){
                FacadeExposed.examDao.insertExam(
                    ExamDto(
                        name = "Elso",
                        questionList = "${tf1?.typeOrdinal}~${tf1?.uuid}#${tf2?.typeOrdinal}~${tf2?.uuid}#${mc1?.typeOrdinal}~${mc1?.uuid}",
                        topicId = topic1?.uuid!!
                    )
                )
                FacadeExposed.examDao.insertExam(
                    ExamDto(
                        name = "Masodik",
                        questionList = "${mc2?.typeOrdinal}~${mc2?.uuid}#${mc1?.typeOrdinal}~${mc1?.uuid}#${tf1?.typeOrdinal}~${tf1?.uuid}",
                        topicId = topic2?.uuid!!
                    )
                )
            }
        }
    }
}

fun main() {
    // run this when you don't want to start the server, just init DB with sample data
    DatabaseFactory.init()
    DatabaseFactory.createSampleData()
}