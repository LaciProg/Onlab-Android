package hu.bme.aut.examappbackend.db.facade

import hu.bme.aut.examappbackend.db.DatabaseFactory.dbQuery
import hu.bme.aut.examappbackend.db.model.TopicDB
import hu.bme.aut.examappbackend.dto.TopicDto
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.UUID

class TopicFacadeExposed : TopicFacade {

    private fun resultRowToTopic(row: ResultRow) = TopicDto(
        uuid = row[TopicDB.id].toString(),
        topic = row[TopicDB.topic],
        description = row[TopicDB.description],
        parentTopic = row[TopicDB.parentTopic].toString()
    )

    override suspend fun getAllTopic(): List<TopicDto> = dbQuery {
        TopicDB.selectAll().map(::resultRowToTopic)
    }

    override suspend fun getAllTopicName(): List<String> = dbQuery {
        TopicDB.selectAll().map{it[TopicDB.topic]}
    }

    override suspend fun getTopicById(uuid: String): TopicDto? = dbQuery {
        TopicDB.selectAll().where(TopicDB.id eq UUID.fromString(uuid))
            .map(::resultRowToTopic)
            .singleOrNull()
    }

    override suspend fun getTopicByTopic(topic: String): TopicDto? = dbQuery {
        TopicDB.selectAll().where(TopicDB.topic eq topic)
            .map(::resultRowToTopic)
            .singleOrNull()
    }

    override suspend fun insertTopic(topicDto: TopicDto): TopicDto? = dbQuery {
        val insertStatement = TopicDB.insert {
            it[topic] = topicDto.topic
            it[description] = topicDto.description
            it[parentTopic] = UUID.fromString(topicDto.parentTopic)
        }
        return@dbQuery insertStatement.resultedValues?.singleOrNull<ResultRow>()?.let(::resultRowToTopic)

    }

    override suspend fun updateTopic(topicDto: TopicDto): Boolean = dbQuery {
        TopicDB.update({ TopicDB.id eq UUID.fromString(topicDto.uuid) }) {
            it[topic] = topicDto.topic
            it[description] = topicDto.description
            it[parentTopic] = UUID.fromString(topicDto.parentTopic)
        } > 0
    }

    override suspend fun deleteTopic(uuid: String): Boolean = dbQuery {
        TopicDB.deleteWhere { TopicDB.id eq UUID.fromString(uuid) } > 0
    }

}