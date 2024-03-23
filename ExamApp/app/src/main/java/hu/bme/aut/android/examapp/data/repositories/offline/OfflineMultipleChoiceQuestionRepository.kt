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
        override fun getMultipleChoiceQuestionsByTopic(topicFk: Int) = multipleChoiceQuestionDao.getMultipleChoiceQuestionsByTopic(topicFk)
        override fun getMultipleChoiceQuestionsByType(typeFk: Int) = multipleChoiceQuestionDao.getMultipleChoiceQuestionsByType(typeFk)
        override fun getAllMultipleChoiceQuestionQuestion() = multipleChoiceQuestionDao.getAllMultipleChoiceQuestionQuestion()
        override fun getMultipleChoiceQuestionByQuestion(question: String) = multipleChoiceQuestionDao.getMultipleChoiceQuestionByQuestion(question)
}