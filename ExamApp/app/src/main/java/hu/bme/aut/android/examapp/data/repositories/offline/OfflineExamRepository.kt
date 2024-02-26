package hu.bme.aut.android.examapp.data.repositories.offline

import hu.bme.aut.android.examapp.data.repositories.inrefaces.ExamRepository
import hu.bme.aut.android.examapp.data.room.dao.ExamDao
import hu.bme.aut.android.examapp.data.room.dto.ExamDto

class OfflineExamRepository(private val examDao: ExamDao) : ExamRepository{
    override suspend fun insertExam(exam: ExamDto) = examDao.insertExam(exam)
    override suspend fun updateExam(exam: ExamDto) = examDao.updateExam(exam)
    override suspend fun deleteExam(exam: ExamDto) = examDao.deleteExam(exam)
    override fun getAllExams() = examDao.getAllExams()
    override fun getExamById(id: Int) = examDao.getExamById(id)
    override fun getExamByName(name: String) = examDao.getExamByName(name)
    override fun getAllExamName() = examDao.getAllExamName()
    override fun getExamsByTopic(topicId: Int) = examDao.getExamsByTopic(topicId)
    override fun getExamNamesByTopic(topicId: Int) = examDao.getExamNamesByTopic(topicId)
}