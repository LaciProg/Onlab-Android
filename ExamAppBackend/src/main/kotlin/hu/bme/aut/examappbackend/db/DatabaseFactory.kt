package hu.bme.aut.examappbackend.db

import enums.Type
import hu.bme.aut.examappbackend.db.facade.FacadeExposed
import hu.bme.aut.examappbackend.db.model.*
import hu.bme.aut.examappbackend.dto.TypeDto
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
            url = System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:5432/examapp",
            driver = System.getenv("DB_DRIVER") ?: "org.postgresql.Driver",
            user = System.getenv("DB_USER") ?: "examapp",
            password = System.getenv("DB_PASSWORD") ?: "examapp"
        )
        transaction(db) {
            SchemaUtils.create(TypeDB)
            SchemaUtils.create(TopicDB)
            SchemaUtils.create(PointDB)
            SchemaUtils.create(TrueFalseQuestionDB)
            SchemaUtils.create(MultipleChoiceQuestionDB)
            SchemaUtils.create(ExamDB)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    fun createSampleData() {
        runBlocking {
            if(FacadeExposed.typeDao.getAllType().isEmpty()){
                val type1 = FacadeExposed.typeDao.insertType(
                    TypeDto(
                        type = Type.trueFalseQuestion.name,
                        uuid = UUID.randomUUID().toString()
                    )
                )
                val type2 = FacadeExposed.typeDao.insertType(
                    TypeDto(
                        type = Type.multipleChoiceQuestion.name,
                        uuid = UUID.randomUUID().toString()
                    )
                )
            }
            /*if (userDAO.allUsers().isEmpty()) {
                val user1 = userDAO.registerNewUser(
                    UserRegistration(
                        "totlaci",
                        "Tóth László",
                        "totlaci@sch.bme.hu"
                    )
                )

                if (user1 != null)
                    userDAO.addNewUserPreferences(user1.uuid)

                val user2 = userDAO.registerNewUser(
                    UserRegistration(
                        "tesztelek",
                        "Teszt Elek",
                        "tesztelek@sch.bme.hu"
                    )
                )
                if (user2 != null)
                    userDAO.addNewUserPreferences(user2.uuid)
            }*/
        
        }
    }
}

fun main() {
    // run this when you don't want to start the server, just init DB with sample data
    DatabaseFactory.init()
    DatabaseFactory.createSampleData()
}