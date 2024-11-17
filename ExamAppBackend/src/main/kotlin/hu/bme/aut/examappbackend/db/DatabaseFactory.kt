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
            url = System.getenv("DB_URL") ?: "jdbc:postgresql://DB:5432/examapp", /*"jdbc:postgresql://152.66.211.35:5432/examapp",*/
            driver = System.getenv("DB_DRIVER") ?: "org.postgresql.Driver",
            user = System.getenv("DB_USER") ?: "examapp",
            password = System.getenv("DB_PASSWORD") ?: "examapp"
        )
        transaction(db) {
            SchemaUtils.drop(ExamDB)
            SchemaUtils.drop(TrueFalseQuestionDB)
            SchemaUtils.drop(MultipleChoiceQuestionDB)
            SchemaUtils.drop(TypeDB)
            SchemaUtils.drop(TopicDB)
            SchemaUtils.drop(PointDB)
            SchemaUtils.drop(UserDB)

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
        var typeTF: TypeDto? = null
        var typeMC: TypeDto? = null
        var pointDefault: PointDto? = null
        var pointWithPenalty: PointDto? = null
        var pointNoPenalty: PointDto? = null
        var topicCompose: TopicDto? = null
        var topicMultiplatform: TopicDto? = null
        var topicComposeMultiplatform: TopicDto? = null
        val tfQuestions: MutableList<TrueFalseQuestionDto?> = mutableListOf()
        val mcQuestions: MutableList<MultipleChoiceQuestionDto?> = mutableListOf()

        runBlocking {
            // Initialize types
            if (FacadeExposed.typeDao.getAllType().isEmpty()) {
                typeTF = FacadeExposed.typeDao.insertType(
                    TypeDto(
                        type = Type.trueFalseQuestion.name,
                        uuid = UUID.randomUUID().toString()
                    )
                )
                typeMC = FacadeExposed.typeDao.insertType(
                    TypeDto(
                        type = Type.multipleChoiceQuestion.name,
                        uuid = UUID.randomUUID().toString()
                    )
                )
            }

            // Initialize points
            if (FacadeExposed.pointDao.getAllPoint().isEmpty()) {
                pointDefault = FacadeExposed.pointDao.insertPoint(
                    PointDto(
                        type = "Default",
                        point = 2.0,
                        goodAnswer = 2.0,
                        badAnswer = -1.0
                    )
                )
                pointWithPenalty = FacadeExposed.pointDao.insertPoint(
                    PointDto(
                        type = "WithPenalty",
                        point = 3.0,
                        goodAnswer = 3.0,
                        badAnswer = -2.0
                    )
                )
                pointNoPenalty = FacadeExposed.pointDao.insertPoint(
                    PointDto(
                        type = "NoPenalty",
                        point = 1.0,
                        goodAnswer = 1.0,
                        badAnswer = 0.0
                    )
                )
            }

            // Initialize topics
            if (FacadeExposed.topicDao.getAllTopic().isEmpty()) {
                topicCompose = FacadeExposed.topicDao.insertTopic(
                    TopicDto(
                        topic = "Jetpack Compose",
                        description = "Jetpack Compose fejlesztési alapok"
                    )
                )
                topicMultiplatform = FacadeExposed.topicDao.insertTopic(
                    TopicDto(
                        topic = "Kotlin Multiplatform",
                        description = "Multiplatform fejlesztés Kotlinban"
                    )
                )
                topicComposeMultiplatform = FacadeExposed.topicDao.insertTopic(
                    TopicDto(
                        topic = "Compose Multiplatform",
                        description = "Compose alapú multiplatform UI fejlesztés"
                    )
                )
            }

            // Initialize True/False questions
            tfQuestions.addAll(
                listOf(
                    // Jetpack Compose
                    FacadeExposed.trueFalseQuestionDao.insertTrueFalseQuestion(
                        TrueFalseQuestionDto(
                            question = "A Jetpack Compose használatához nem szükséges XML fájlokat írni.",
                            correctAnswer = true,
                            point = pointDefault?.uuid!!,
                            topic = topicCompose?.uuid!!,
                            type = typeTF?.uuid!!
                        )
                    ),
                    FacadeExposed.trueFalseQuestionDao.insertTrueFalseQuestion(
                        TrueFalseQuestionDto(
                            question = "A Jetpack Compose automatikusan kezeli az állapotváltozásokat.",
                            correctAnswer = true,
                            point = pointNoPenalty?.uuid!!,
                            topic = topicCompose?.uuid!!,
                            type = typeTF?.uuid!!
                        )
                    ),
                    FacadeExposed.trueFalseQuestionDao.insertTrueFalseQuestion(
                        TrueFalseQuestionDto(
                            question = "A Composable annotáció nélküli függvények mindig működnek Compose-ban.",
                            correctAnswer = false,
                            point = pointWithPenalty?.uuid!!,
                            topic = topicCompose?.uuid!!,
                            type = typeTF?.uuid!!
                        )
                    ),
                    FacadeExposed.trueFalseQuestionDao.insertTrueFalseQuestion(
                        TrueFalseQuestionDto(
                            question = "A Jetpack Compose a View alapú rendszert váltotta le teljesen.",
                            correctAnswer = false,
                            point = pointDefault?.uuid!!,
                            topic = topicCompose?.uuid!!,
                            type = typeTF?.uuid!!
                        )
                    ),
                    // Kotlin Multiplatform
                    FacadeExposed.trueFalseQuestionDao.insertTrueFalseQuestion(
                        TrueFalseQuestionDto(
                            question = "A Kotlin Multiplatform támogatja a JavaScript környezeteket.",
                            correctAnswer = true,
                            point = pointDefault?.uuid!!,
                            topic = topicMultiplatform?.uuid!!,
                            type = typeTF?.uuid!!
                        )
                    ),
                    FacadeExposed.trueFalseQuestionDao.insertTrueFalseQuestion(
                        TrueFalseQuestionDto(
                            question = "A Kotlin Multiplatform segítségével teljes Android appokat fejleszthetünk.",
                            correctAnswer = true,
                            point = pointNoPenalty?.uuid!!,
                            topic = topicMultiplatform?.uuid!!,
                            type = typeTF?.uuid!!
                        )
                    ),
                    FacadeExposed.trueFalseQuestionDao.insertTrueFalseQuestion(
                        TrueFalseQuestionDto(
                            question = "A shared modulban használt kódrészletek nem érhetők el az Android platformon.",
                            correctAnswer = false,
                            point = pointWithPenalty?.uuid!!,
                            topic = topicMultiplatform?.uuid!!,
                            type = typeTF?.uuid!!
                        )
                    ),
                    FacadeExposed.trueFalseQuestionDao.insertTrueFalseQuestion(
                        TrueFalseQuestionDto(
                            question = "A multiplatform könyvtárak írásához feltétlenül szükséges a Kotlin Native használata.",
                            correctAnswer = false,
                            point = pointDefault?.uuid!!,
                            topic = topicMultiplatform?.uuid!!,
                            type = typeTF?.uuid!!
                        )
                    ),
                    // Compose Multiplatform
                    FacadeExposed.trueFalseQuestionDao.insertTrueFalseQuestion(
                        TrueFalseQuestionDto(
                            question = "A Compose Multiplatform az Android Compose kiterjesztése más platformokra.",
                            correctAnswer = true,
                            point = pointWithPenalty?.uuid!!,
                            topic = topicComposeMultiplatform?.uuid!!,
                            type = typeTF?.uuid!!
                        )
                    ),
                    FacadeExposed.trueFalseQuestionDao.insertTrueFalseQuestion(
                        TrueFalseQuestionDto(
                            question = "A Compose Multiplatform kód nem fordítható iOS platformra.",
                            correctAnswer = false,
                            point = pointDefault?.uuid!!,
                            topic = topicComposeMultiplatform?.uuid!!,
                            type = typeTF?.uuid!!
                        )
                    ),
                    FacadeExposed.trueFalseQuestionDao.insertTrueFalseQuestion(
                        TrueFalseQuestionDto(
                            question = "A Compose Multiplatform jelenleg nem támogatja a Web platformot.",
                            correctAnswer = false,
                            point = pointNoPenalty?.uuid!!,
                            topic = topicComposeMultiplatform?.uuid!!,
                            type = typeTF?.uuid!!
                        )
                    ),
                    FacadeExposed.trueFalseQuestionDao.insertTrueFalseQuestion(
                        TrueFalseQuestionDto(
                            question = "A Compose Multiplatform előnye a közös UI-kód megosztása több platform között.",
                            correctAnswer = true,
                            point = pointWithPenalty?.uuid!!,
                            topic = topicComposeMultiplatform?.uuid!!,
                            type = typeTF?.uuid!!
                        )
                    )
                )
            )

            mcQuestions.addAll(
                listOf(
                    // Jetpack Compose
                    FacadeExposed.multipleChoiceQuestionDao.insertMultipleChoiceQuestion(
                        MultipleChoiceQuestionDto(
                            question = "Melyek Jetpack Compose alapfogalmai?",
                            answers = listOf("Composable", "State", "Layout", "Fragment"),
                            correctAnswersList = listOf("Composable", "State", "Layout"),
                            point = pointDefault?.uuid!!,
                            topic = topicCompose?.uuid!!,
                            type = typeMC?.uuid!!
                        )
                    ),
                    FacadeExposed.multipleChoiceQuestionDao.insertMultipleChoiceQuestion(
                        MultipleChoiceQuestionDto(
                            question = "Milyen eszközöket használhatunk Jetpack Compose fejlesztéshez?",
                            answers = listOf("Android Studio", "Kotlin", "XML", "LiveData"),
                            correctAnswersList = listOf("Android Studio", "Kotlin", "LiveData"),
                            point = pointWithPenalty?.uuid!!,
                            topic = topicCompose?.uuid!!,
                            type = typeMC?.uuid!!
                        )
                    ),
                    FacadeExposed.multipleChoiceQuestionDao.insertMultipleChoiceQuestion(
                        MultipleChoiceQuestionDto(
                            question = "Melyik Jetpack Compose függvény használható listák megjelenítésére?",
                            answers = listOf("LazyColumn", "LazyRow", "ListView", "RecyclerView"),
                            correctAnswersList = listOf("LazyColumn", "LazyRow"),
                            point = pointNoPenalty?.uuid!!,
                            topic = topicCompose?.uuid!!,
                            type = typeMC?.uuid!!
                        )
                    ),
                    // Kotlin Multiplatform
                    FacadeExposed.multipleChoiceQuestionDao.insertMultipleChoiceQuestion(
                        MultipleChoiceQuestionDto(
                            question = "Mely platformokat támogatja a Kotlin Multiplatform?",
                            answers = listOf("Android", "iOS", "Windows", "Linux", "Web"),
                            correctAnswersList = listOf("Android", "iOS", "Web"),
                            point = pointDefault?.uuid!!,
                            topic = topicMultiplatform?.uuid!!,
                            type = typeMC?.uuid!!
                        )
                    ),
                    FacadeExposed.multipleChoiceQuestionDao.insertMultipleChoiceQuestion(
                        MultipleChoiceQuestionDto(
                            question = "Melyik könyvtárakat használhatjuk multiplatform projektben?",
                            answers = listOf("Ktor", "SQLDelight", "Jetpack Navigation", "Coroutines"),
                            correctAnswersList = listOf("Ktor", "SQLDelight", "Coroutines"),
                            point = pointWithPenalty?.uuid!!,
                            topic = topicMultiplatform?.uuid!!,
                            type = typeMC?.uuid!!
                        )
                    ),
                    FacadeExposed.multipleChoiceQuestionDao.insertMultipleChoiceQuestion(
                        MultipleChoiceQuestionDto(
                            question = "Melyik nyelvi funkciók fontosak a multiplatform kódíráshoz?",
                            answers = listOf("expect/actual", "data class", "sealed class", "inline class"),
                            correctAnswersList = listOf("expect/actual", "sealed class", "data class"),
                            point = pointNoPenalty?.uuid!!,
                            topic = topicMultiplatform?.uuid!!,
                            type = typeMC?.uuid!!
                        )
                    ),
                    // Compose Multiplatform
                    FacadeExposed.multipleChoiceQuestionDao.insertMultipleChoiceQuestion(
                        MultipleChoiceQuestionDto(
                            question = "Mely platformokat támogatja a Compose Multiplatform?",
                            answers = listOf("Android", "Web", "iOS", "Desktop", "Smart TV"),
                            correctAnswersList = listOf("Android", "Web", "iOS", "Desktop"),
                            point = pointDefault?.uuid!!,
                            topic = topicComposeMultiplatform?.uuid!!,
                            type = typeMC?.uuid!!
                        )
                    ),
                    FacadeExposed.multipleChoiceQuestionDao.insertMultipleChoiceQuestion(
                        MultipleChoiceQuestionDto(
                            question = "Miért érdemes Compose Multiplatformot használni?",
                            answers = listOf(
                                "UI megosztás",
                                "Natív platformokhoz közelálló élmény",
                                "Gyors fejlesztés",
                                "Csak Android támogatás"
                            ),
                            correctAnswersList = listOf(
                                "UI megosztás",
                                "Natív platformokhoz közelálló élmény",
                                "Gyors fejlesztés"
                            ),
                            point = pointWithPenalty?.uuid!!,
                            topic = topicComposeMultiplatform?.uuid!!,
                            type = typeMC?.uuid!!
                        )
                    ),
                    FacadeExposed.multipleChoiceQuestionDao.insertMultipleChoiceQuestion(
                        MultipleChoiceQuestionDto(
                            question = "Milyen komponenseket nyújt a Compose Multiplatform?",
                            answers = listOf("Button", "TextField", "Image", "Fragment"),
                            correctAnswersList = listOf("Button", "TextField", "Image"),
                            point = pointNoPenalty?.uuid!!,
                            topic = topicComposeMultiplatform?.uuid!!,
                            type = typeMC?.uuid!!
                        )
                    )
                )
            )
        }



        runBlocking {
            // Jetpack Compose kezdő vizsga
            val jetpackComposeBeginnerExam = FacadeExposed.examDao.insertExam(
                ExamDto(
                    name = "Jetpack Compose Kezdő Vizsga",
                    questionList = listOf(
                        // TF kérdések: 1, 2, 3, 4
                        "${tfQuestions[0]?.typeOrdinal}~${tfQuestions[0]?.uuid}",
                        "${tfQuestions[1]?.typeOrdinal}~${tfQuestions[1]?.uuid}",
                        "${tfQuestions[2]?.typeOrdinal}~${tfQuestions[2]?.uuid}",
                        "${tfQuestions[3]?.typeOrdinal}~${tfQuestions[3]?.uuid}",
                        // MC kérdések: 1, 2
                        "${mcQuestions[0]?.typeOrdinal}~${mcQuestions[0]?.uuid}",
                        "${mcQuestions[1]?.typeOrdinal}~${mcQuestions[1]?.uuid}",
                    ).joinToString("#") { it },
                    topicId = topicCompose?.uuid!!  // A téma az adott vizsga témája
                )
            )

            // Kotlin Multiplatform középhaladó vizsga
            val kotlinMultiplatformIntermediateExam = FacadeExposed.examDao.insertExam(
                ExamDto(
                    name = "Kotlin Multiplatform Középhaladó Vizsga",
                    questionList = listOf(
                        // TF kérdések: 5, 6, 7, 8
                        "${tfQuestions[4]?.typeOrdinal}~${tfQuestions[4]?.uuid}",
                        "${tfQuestions[5]?.typeOrdinal}~${tfQuestions[5]?.uuid}",
                        "${tfQuestions[6]?.typeOrdinal}~${tfQuestions[6]?.uuid}",
                        "${tfQuestions[7]?.typeOrdinal}~${tfQuestions[7]?.uuid}",
                        // MC kérdések: 5, 6
                        "${mcQuestions[4]?.typeOrdinal}~${mcQuestions[4]?.uuid}",
                        "${mcQuestions[5]?.typeOrdinal}~${mcQuestions[5]?.uuid}",
                    ).joinToString("#") { it },
                    topicId = topicMultiplatform?.uuid!!  // A téma az adott vizsga témája
                )
            )

            // Compose Multiplatform haladó vizsga
            val composeMultiplatformAdvancedExam = FacadeExposed.examDao.insertExam(
                ExamDto(
                    name = "Compose Multiplatform Haladó Vizsga",
                    questionList = listOf(
                        // TF kérdések: 9, 10, 11, 12
                        "${tfQuestions[8]?.typeOrdinal}~${tfQuestions[8]?.uuid}",
                        "${tfQuestions[9]?.typeOrdinal}~${tfQuestions[9]?.uuid}",
                        "${tfQuestions[10]?.typeOrdinal}~${tfQuestions[10]?.uuid}",
                        "${tfQuestions[11]?.typeOrdinal}~${tfQuestions[11]?.uuid}",
                        // MC kérdések: 7, 8
                        "${mcQuestions[6]?.typeOrdinal}~${mcQuestions[6]?.uuid}",
                        "${mcQuestions[7]?.typeOrdinal}~${mcQuestions[7]?.uuid}",
                    ).joinToString("#") { it },
                    topicId = topicComposeMultiplatform?.uuid!!  // A téma az adott vizsga témája
                )
            )

            // Komplex vizsga (összesített)
            val complexExam = FacadeExposed.examDao.insertExam(
                ExamDto(
                    name = "Komplex Vizsga",
                    questionList = listOf(
                        // TF kérdések: 1–12 (véletlenszerű sorrendben)
                        tfQuestions.shuffled().take(12).map { "${it?.typeOrdinal}~${it?.uuid}" },
                        // MC kérdések: 1–8 (véletlenszerű sorrendben)
                        mcQuestions.shuffled().take(8).map { "${it?.typeOrdinal}~${it?.uuid}" }
                    ).flatten().joinToString("#") { it },
                    topicId = topicCompose?.uuid!! // Vagy választhatsz a teljes vizsga számára egy főbb témát
                )
            )
        }

    }
}

fun main() {
    // run this when you don't want to start the server, just init DB with sample data
    DatabaseFactory.init()
    DatabaseFactory.createSampleData()
}