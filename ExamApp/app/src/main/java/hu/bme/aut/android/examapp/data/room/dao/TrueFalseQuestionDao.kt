package hu.bme.aut.android.examapp.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hu.bme.aut.android.examapp.data.room.dto.TopicDto
import hu.bme.aut.android.examapp.data.room.dto.TrueFalseQuestionDto
import kotlinx.coroutines.flow.Flow

@Dao
interface TrueFalseQuestionDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTrueFalseQuestion(trueFalseQuestion: TrueFalseQuestionDto)

    @Update
    suspend fun updateTrueFalseQuestion(trueFalseQuestion: TrueFalseQuestionDto)

    @Delete
    suspend fun deleteTrueFalseQuestion(trueFalseQuestion: TrueFalseQuestionDto)

    @Query("SELECT * FROM trueFalseQuestion")
    fun getAllTrueFalseQuestions(): Flow<List<TrueFalseQuestionDto>>

    @Query("SELECT * FROM trueFalseQuestion WHERE id = :id")
    fun getTrueFalseQuestionById(id: Int): Flow<TrueFalseQuestionDto>

    @Query("SELECT * FROM trueFalseQuestion" +
            " JOIN topic ON trueFalseQuestion.topicId = topic.id" +
            " WHERE topic.topic = :topic OR topic.parentTopic = :topic")
    fun getTrueFalseQuestionsByTopic(topic: String): Flow<Map<TopicDto ,List<TrueFalseQuestionDto>>>

    @Query("SELECT * FROM trueFalseQuestion" +
            " JOIN type ON trueFalseQuestion.topicId = type.id" +
            " WHERE type.id = :typeId")
    fun getTrueFalseQuestionsByType(typeId: Int): Flow<List<TrueFalseQuestionDto>>

    @Query("SELECT question FROM trueFalseQuestion ORDER BY question ASC")
    fun getAllTrueFalseQuestionQuestion(): Flow<List<String>>
}