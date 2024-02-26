package hu.bme.aut.android.examapp.data.repositories.offline

import hu.bme.aut.android.examapp.data.repositories.inrefaces.MultipleChoiceQuestionRepository
import hu.bme.aut.android.examapp.data.room.dao.MultipleChoiceQuestionDao
import hu.bme.aut.android.examapp.data.room.dto.MultipleChoiceQuestionDto

class OfflineMultipleChoiceQuestionRepository(private val multipleChoiceQuestionDao: MultipleChoiceQuestionDao) :
    MultipleChoiceQuestionRepository {
    override suspend fun insertMultipleChoiceQuestion(multipleChoiceQuestion: MultipleChoiceQuestionDto) =
        multipleChoiceQuestionDao.insertMultipleChoiceQuestion(multipleChoiceQuestion)
        override suspend fun updateMultipleChoiceQuestion(multipleChoiceQuestion: MultipleChoiceQuestionDto) =
        multipleChoiceQuestionDao.updateMultipleChoiceQuestion(multipleChoiceQuestion)
        override suspend fun deleteMultipleChoiceQuestion(multipleChoiceQuestion: MultipleChoiceQuestionDto) =
        multipleChoiceQuestionDao.deleteMultipleChoiceQuestion(multipleChoiceQuestion)
        override fun getAllMultipleChoiceQuestions() = multipleChoiceQuestionDao.getAllMultipleChoiceQuestions()
        override fun getMultipleChoiceQuestionById(id: Int) = multipleChoiceQuestionDao.getMultipleChoiceQuestionById(id)
        override fun getMultipleChoiceQuestionsByTopic(topicId: Int) = multipleChoiceQuestionDao.getMultipleChoiceQuestionsByTopic(topicId)
        override fun getMultipleChoiceQuestionsByType(typeId: Int) = multipleChoiceQuestionDao.getMultipleChoiceQuestionsByType(typeId)
        override fun getAllMultipleChoiceQuestionQuestion() = multipleChoiceQuestionDao.getAllMultipleChoiceQuestionQuestion()
}