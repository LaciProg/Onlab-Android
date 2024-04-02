package hu.bme.aut.examappbackend.db.facade

import hu.bme.aut.examappbackend.dto.TrueFalseQuestionDto

interface TrueFalseQuestionFacade {
    suspend fun getAllTrueFalseQuestion() : List<TrueFalseQuestionDto>
    suspend fun getTrueFalseQuestionById(uuid: String) : TrueFalseQuestionDto?
    suspend fun deleteTrueFalseQuestion(trueFalseQuestion: TrueFalseQuestionDto) : Boolean
    suspend fun insertTrueFalseQuestion(trueFalseQuestion: TrueFalseQuestionDto) : TrueFalseQuestionDto?
    suspend fun updateTrueFalseQuestion(trueFalseQuestion: TrueFalseQuestionDto) : TrueFalseQuestionDto?
    suspend fun getAllTrueFalseQuestionQuestion() : List<String>
}