package hu.bme.aut.android.examapp.data.repositories.offline

import hu.bme.aut.android.examapp.data.repositories.inrefaces.TrueFalseQuestionRepository
import hu.bme.aut.android.examapp.data.room.dao.TrueFalseQuestionDao
import hu.bme.aut.android.examapp.data.room.dto.TopicDto
import hu.bme.aut.android.examapp.data.room.dto.TrueFalseQuestionDto
import kotlinx.coroutines.flow.Flow


class OfflineTrueFalseQuestionRepository(private val trueFalseQuestionDao: TrueFalseQuestionDao) :
    TrueFalseQuestionRepository {
        override suspend fun insertTrueFalseQuestion(trueFalseQuestion: TrueFalseQuestionDto) =
            trueFalseQuestionDao.insertTrueFalseQuestion(trueFalseQuestion)
       override suspend fun updateTrueFalseQuestion(trueFalseQuestion: TrueFalseQuestionDto) =
            trueFalseQuestionDao.updateTrueFalseQuestion(trueFalseQuestion)
       override suspend fun deleteTrueFalseQuestion(trueFalseQuestion: TrueFalseQuestionDto) =
            trueFalseQuestionDao.deleteTrueFalseQuestion(trueFalseQuestion)
       override fun getAllTrueFalseQuestions(): Flow<List<TrueFalseQuestionDto>> = trueFalseQuestionDao.getAllTrueFalseQuestions()
       override fun getTrueFalseQuestionById(id: Int): Flow<TrueFalseQuestionDto> = trueFalseQuestionDao.getTrueFalseQuestionById(id)
       override fun getTrueFalseQuestionsByTopic(topic: String): Flow<Map<TopicDto, List<TrueFalseQuestionDto>>> = trueFalseQuestionDao.getTrueFalseQuestionsByTopic(topic)
       override fun getTrueFalseQuestionsByType(typeId: Int): Flow<List<TrueFalseQuestionDto>> = trueFalseQuestionDao.getTrueFalseQuestionsByType(typeId)
       override fun getAllTrueFalseQuestionQuestion() = trueFalseQuestionDao.getAllTrueFalseQuestionQuestion()

}