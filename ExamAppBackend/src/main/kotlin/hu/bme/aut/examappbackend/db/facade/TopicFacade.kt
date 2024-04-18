package hu.bme.aut.examappbackend.db.facade

import hu.bme.aut.examappbackend.dto.NameDto
import hu.bme.aut.examappbackend.dto.TopicDto

interface TopicFacade {
    suspend fun getAllTopic() : List<TopicDto>
    suspend fun getTopicById(uuid: String) : TopicDto?
    suspend fun insertTopic(topicDto: TopicDto) : TopicDto?
    suspend fun updateTopic(topicDto: TopicDto) : Boolean
    suspend fun deleteTopic(uuid: String) : Boolean
    suspend fun getAllTopicName() : List<NameDto>
    suspend fun getTopicByTopic(topic: String) : TopicDto?
}