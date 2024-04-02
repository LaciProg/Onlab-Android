package hu.bme.aut.examappbackend.db.facade

import hu.bme.aut.examappbackend.dto.TopicDto

interface TopicFacade {
    suspend fun getAppTopic() : List<TopicDto>
    suspend fun getTopicById(uuid: String) : TopicDto?
    suspend fun insertTopic(topicDto: TopicDto) : TopicDto?
    suspend fun updateTopic(topicDto: TopicDto) : TopicDto?
    suspend fun deleteTopic(topicDto: TopicDto) : Boolean
    suspend fun getAllTopicName() : List<String>
    suspend fun getTopicByTopic(topic: String) : TopicDto?
}