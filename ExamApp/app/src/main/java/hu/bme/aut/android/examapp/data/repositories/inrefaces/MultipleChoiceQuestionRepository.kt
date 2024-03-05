package hu.bme.aut.android.examapp.data.repositories.inrefaces

import hu.bme.aut.android.examapp.data.room.dto.MultipleChoiceQuestionDto
import hu.bme.aut.android.examapp.data.room.dto.TopicDto
import kotlinx.coroutines.flow.Flow

interface MultipleChoiceQuestionRepository {
    suspend fun insertMultipleChoiceQuestion(multipleChoiceQuestion: MultipleChoiceQuestionDto)
    suspend fun updateMultipleChoiceQuestion(multipleChoiceQuestion: MultipleChoiceQuestionDto)
    suspend fun deleteMultipleChoiceQuestion(multipleChoiceQuestion: MultipleChoiceQuestionDto)
    fun getAllMultipleChoiceQuestions(): Flow<List<MultipleChoiceQuestionDto>>
    fun getMultipleChoiceQuestionById(id: Int): Flow<MultipleChoiceQuestionDto>
    fun getMultipleChoiceQuestionsByTopic(topic: String): Flow<Map<TopicDto,List<MultipleChoiceQuestionDto>>>
    fun getMultipleChoiceQuestionsByType(typeId: Int): Flow<List<MultipleChoiceQuestionDto>>
    fun getAllMultipleChoiceQuestionQuestion(): Flow<List<String>>

}