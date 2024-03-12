package hu.bme.aut.android.examapp.data.repositories.offline

import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.data.room.dao.TopicDao
import hu.bme.aut.android.examapp.data.room.dto.TopicDto
import kotlinx.coroutines.flow.Flow

class OfflineTopicRepository(private val topicDao: TopicDao) : TopicRepository {
    override suspend fun insertTopic(topic: TopicDto) = topicDao.insertTopic(topic)
    override suspend fun updateTopic(topic: TopicDto) = topicDao.updateTopic(topic)
    override suspend fun deleteTopic(topic: TopicDto) = topicDao.deleteTopic(topic) //TODO minden hivatkozást törölni
    override fun getAllTopics() = topicDao.getAllTopics()
    override fun getTopicById(id: Int) = topicDao.getTopicById(id)
    override fun getAllTopicName() = topicDao.getAllTopicName()
    override fun getTopicsByParentId(parentTopicFk: Int) = topicDao.getTopicsByParentId(parentTopicFk)
    override fun getTopicByTopic(topic: String): Flow<TopicDto> = topicDao.getTopicByTopic(topic)
}