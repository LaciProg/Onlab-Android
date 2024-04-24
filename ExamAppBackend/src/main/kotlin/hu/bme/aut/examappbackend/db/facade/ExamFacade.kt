package hu.bme.aut.examappbackend.db.facade

import hu.bme.aut.examappbackend.dto.ExamDto
import hu.bme.aut.examappbackend.dto.Question

interface ExamFacade {
    suspend fun getAllExam() : List<ExamDto>
    suspend fun getAllExamNames() : List<String>
    suspend fun getAllQuestionString(exam: String): String?
    suspend fun getAllQuestion(): List<Question>
    suspend fun getAllQuestionId(): List<String>

    suspend fun getExamById(uuid: String) : ExamDto?
    suspend fun getExamByName(exam: String): ExamDto?
    suspend fun deleteExam(uuid: String) : Boolean
    suspend fun insertExam(exam: ExamDto) : ExamDto?
    suspend fun updateExam(exam: ExamDto) : Boolean
}