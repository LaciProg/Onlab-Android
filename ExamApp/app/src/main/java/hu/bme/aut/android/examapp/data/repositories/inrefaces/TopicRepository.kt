package hu.bme.aut.android.examapp.data.repositories.inrefaces

import hu.bme.aut.android.examapp.data.room.dto.TopicDto
import kotlinx.coroutines.flow.Flow

interface TopicRepository {
    suspend fun insertTopic(topic: TopicDto)
    suspend fun updateTopic(topic: TopicDto)
    suspend fun deleteTopic(topic: TopicDto)
    fun getAllTopics(): Flow<List<TopicDto>>
    fun getTopicById(id: Int): Flow<TopicDto>
    fun getAllTopicName(): Flow<List<String>>
    fun getTopicsByParentId(parentTopic: String): Flow<List<TopicDto>>
    fun getTopicByTopic(topic: String): Flow<TopicDto>
}