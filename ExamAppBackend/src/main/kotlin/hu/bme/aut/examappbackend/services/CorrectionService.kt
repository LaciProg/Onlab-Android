package hu.bme.aut.examappbackend.services

import enums.Type
import hu.bme.aut.examappbackend.db.facade.FacadeExposed
import hu.bme.aut.examappbackend.dto.*
import kotlinx.coroutines.runBlocking

class CorrectionService {

    suspend fun getQuestions(exam: String): List<Question>? {
        val questionString = FacadeExposed.examDao.getAllQuestionString(exam)
        val questions = questionString?.split("#") ?: return null
        val questionList: MutableList<Question> = mutableListOf()
        for(question in questions){
            when(question.substringBefore("~").toInt()){
                Type.trueFalseQuestion.ordinal -> {
                    val q = FacadeExposed.trueFalseQuestionDao.getTrueFalseQuestionById(question.substringAfter("~"))
                    if(q != null){ questionList.add(q) }
                }
                Type.multipleChoiceQuestion.ordinal -> {
                    val q = FacadeExposed.multipleChoiceQuestionDao.getMultipleChoiceQuestionById(question.substringAfter("~"))
                    if(q != null){ questionList.add(q) }
                }
            }
        }
        return questionList
    }

    private suspend fun getPoints() =
        FacadeExposed.pointDao.getAllPoint()

    fun correcting(exam: String, answers: List<List<String>>): StatisticsDto?{
        val questions: MutableList<Question> = mutableListOf()
        var points: List<PointDto> = mutableListOf()
        runBlocking {
            val q = getQuestions(exam)
            if(q != null){
                questions.addAll(q)
            }
            points = getPoints()
        }
        if(questions.isEmpty()) return null
        var pointsGained = 0.0
        var maxPoint = 0.0
        for((index, question) in questions.withIndex()){
            if(question is TrueFalseQuestionDto){
                if(question.correctAnswer.toString() == answers[index][0]){
                    pointsGained += points.find { it.uuid == question.point }?.goodAnswer!!
                } else {
                    pointsGained -= points.find { it.uuid == question.point }?.badAnswer!!
                }
                maxPoint += points.find { it.uuid == question.point }?.point!!
            }
            else if (question is MultipleChoiceQuestionDto){
                val badAnswers: MutableList<String> = mutableListOf()
                question.answers.forEach { if(!question.correctAnswersList.contains(it)) badAnswers.add(it) }
                for(answer in answers[index]){
                    if(question.correctAnswersList.contains(answer)){
                        pointsGained += points.find { it.uuid == question.point }?.goodAnswer!!
                    } else if(badAnswers.contains(answer)) {
                        pointsGained -= points.find { it.uuid == question.point }?.badAnswer!!
                    }
                }
                maxPoint += points.find { it.uuid == question.point }?.point!!
            }

        }
        return StatisticsDto(
            earnedPoints = pointsGained,
            percentage = pointsGained/maxPoint
        )
    }

    private fun notChosenRight(badAnswers: List<String>, answers: List<String>): Boolean{
            for(answer in answers){
                if(badAnswers.contains(answer)) return false
            }
        return true
    }

}