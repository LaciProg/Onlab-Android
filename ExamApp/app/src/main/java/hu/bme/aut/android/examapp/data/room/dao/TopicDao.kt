package hu.bme.aut.android.examapp.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hu.bme.aut.android.examapp.data.room.dto.TopicDto
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTopic(topic: TopicDto)

    @Update
    suspend fun updateTopic(topic: TopicDto)

    @Delete
    suspend fun deleteTopic(topic: TopicDto)

    @Query("SELECT * FROM topic")
    fun getAllTopics(): Flow<List<TopicDto>>

    @Query("SELECT * FROM topic WHERE id = :id")
    fun getTopicById(id: Int): Flow<TopicDto>

    @Query("SELECT * FROM topic WHERE parentTopic = :parentTopic")
    fun getTopicsByParentId(parentTopic: String): Flow<List<TopicDto>>

    @Query("SELECT * FROM topic WHERE topic = :topic")
    fun getTopicByTopic(topic: String): Flow<TopicDto>

    @Query("SELECT topic FROM topic ORDER BY topic ASC")
    fun getAllTopicName(): Flow<List<String>>
}