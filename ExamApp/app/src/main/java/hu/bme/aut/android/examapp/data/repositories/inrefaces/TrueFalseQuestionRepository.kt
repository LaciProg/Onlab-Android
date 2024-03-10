package hu.bme.aut.android.examapp.data.repositories.inrefaces

import hu.bme.aut.android.examapp.data.room.dto.TopicDto
import hu.bme.aut.android.examapp.data.room.dto.TrueFalseQuestionDto
import kotlinx.coroutines.flow.Flow

interface TrueFalseQuestionRepository {
    suspend fun insertTrueFalseQuestion(trueFalseQuestion: TrueFalseQuestionDto)
    suspend fun updateTrueFalseQuestion(trueFalseQuestion: TrueFalseQuestionDto)
    suspend fun deleteTrueFalseQuestion(trueFalseQuestion: TrueFalseQuestionDto)
    fun getAllTrueFalseQuestions(): Flow<List<TrueFalseQuestionDto>>
    fun getTrueFalseQuestionById(id: Int): Flow<TrueFalseQuestionDto>
    fun getTrueFalseQuestionsByTopic(topicFk: Int): Flow<Map<TopicDto,List<TrueFalseQuestionDto>>>
    fun getTrueFalseQuestionsByType(typeFk: Int): Flow<List<TrueFalseQuestionDto>>
    fun getAllTrueFalseQuestionQuestion(): Flow<List<String>>

}