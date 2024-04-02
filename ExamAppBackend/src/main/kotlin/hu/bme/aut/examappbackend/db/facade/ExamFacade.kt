package hu.bme.aut.examappbackend.db.facade

import hu.bme.aut.examappbackend.dto.ExamDto

interface ExamFacade {
    suspend fun getAllExam() : List<ExamDto>
    suspend fun getAllExamNames() : List<String>
    suspend fun getExamById(uuid: String) : ExamDto?
    suspend fun deleteExam(uuid: String) : Boolean
    suspend fun insertExam(exam: ExamDto) : ExamDto?
    suspend fun updateExam(exam: ExamDto) : Boolean
}