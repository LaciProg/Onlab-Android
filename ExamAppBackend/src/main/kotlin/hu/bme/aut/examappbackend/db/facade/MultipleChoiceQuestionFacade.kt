package hu.bme.aut.examappbackend.db.facade

import hu.bme.aut.examappbackend.dto.MultipleChoiceQuestionDto

interface MultipleChoiceQuestionFacade {
    suspend fun getAllMultipleChoiceQuestion() : List<MultipleChoiceQuestionDto>
    suspend fun getMultipleChoiceQuestionById(uuid: String) : MultipleChoiceQuestionDto?
    suspend fun deleteMultipleChoiceQuestion(multipleChoiceQuestion: MultipleChoiceQuestionDto) : Boolean
    suspend fun insertMultipleChoiceQuestion(multipleChoiceQuestion: MultipleChoiceQuestionDto) : MultipleChoiceQuestionDto?
    suspend fun updateMultipleChoiceQuestion(multipleChoiceQuestion: MultipleChoiceQuestionDto) : MultipleChoiceQuestionDto?
    suspend fun getAllMultipleChoiceQuestionQuestion() : List<String>
}