package hu.bme.aut.examappbackend.db.facade

import hu.bme.aut.examappbackend.dto.MultipleChoiceQuestionDto
import hu.bme.aut.examappbackend.dto.NameDto

interface MultipleChoiceQuestionFacade {
    suspend fun getAllMultipleChoiceQuestion() : List<MultipleChoiceQuestionDto>
    suspend fun getMultipleChoiceQuestionById(uuid: String) : MultipleChoiceQuestionDto?
    suspend fun deleteMultipleChoiceQuestion(uuid: String) : Boolean
    suspend fun insertMultipleChoiceQuestion(multipleChoiceQuestion: MultipleChoiceQuestionDto) : MultipleChoiceQuestionDto?
    suspend fun updateMultipleChoiceQuestion(multipleChoiceQuestion: MultipleChoiceQuestionDto) : Boolean
    suspend fun getAllMultipleChoiceQuestionQuestion() : List<NameDto>
}