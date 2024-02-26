package hu.bme.aut.android.examapp.data.repositories.inrefaces

import hu.bme.aut.android.examapp.data.room.dto.ExamDto
import kotlinx.coroutines.flow.Flow

interface ExamRepository {
    suspend fun insertExam(exam: ExamDto)
    suspend fun updateExam(exam: ExamDto)
    suspend fun deleteExam(exam: ExamDto)
    fun getAllExams(): Flow<List<ExamDto>>
    fun getExamById(id: Int): Flow<ExamDto>
    fun getExamByName(name: String): Flow<ExamDto>
    fun getAllExamName(): Flow<List<String>>
    fun getExamsByTopic(topicId: Int): Flow<List<ExamDto>>
    fun getExamNamesByTopic(topicId: Int): Flow<List<String>>
}