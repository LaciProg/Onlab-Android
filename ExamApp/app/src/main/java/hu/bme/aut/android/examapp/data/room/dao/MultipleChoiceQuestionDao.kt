package hu.bme.aut.android.examapp.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hu.bme.aut.android.examapp.data.room.dto.MultipleChoiceQuestionDto
import hu.bme.aut.android.examapp.data.room.dto.TopicDto
import kotlinx.coroutines.flow.Flow

@Dao
interface MultipleChoiceQuestionDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertMultipleChoiceQuestion(multipleChoiceQuestion: MultipleChoiceQuestionDto)

    @Update
    suspend fun updateMultipleChoiceQuestion(multipleChoiceQuestion: MultipleChoiceQuestionDto)

    @Delete
    suspend fun deleteMultipleChoiceQuestion(multipleChoiceQuestion: MultipleChoiceQuestionDto)

    @Query("SELECT * FROM multipleChoiceQuestion")
    fun getAllMultipleChoiceQuestions(): Flow<List<MultipleChoiceQuestionDto>>

    @Query("SELECT * FROM multipleChoiceQuestion WHERE id = :id")
    fun getMultipleChoiceQuestionById(id: Int): Flow<MultipleChoiceQuestionDto>

    @Query("SELECT * FROM multipleChoiceQuestion" +
            " JOIN topic ON multipleChoiceQuestion.topicId = topic.id" +
            " WHERE topic.id = :topicId OR topic.parentId = :topicId")
    fun getMultipleChoiceQuestionsByTopic(topicId: Int): Flow<Map<TopicDto ,List<MultipleChoiceQuestionDto>>>

    @Query("SELECT * FROM multipleChoiceQuestion" +
            " JOIN type ON multipleChoiceQuestion.topicId = type.id" +
            " WHERE type.id = :typeId")
    fun getMultipleChoiceQuestionsByType(typeId: Int): Flow<List<MultipleChoiceQuestionDto>>

    @Query("SELECT question FROM multipleChoiceQuestion ORDER BY question ASC")
    fun getAllMultipleChoiceQuestionQuestion(): Flow<List<String>>
}