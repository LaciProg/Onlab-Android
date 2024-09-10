package hu.bme.aut.examappbackend.services

import enums.Type
import hu.bme.aut.examappbackend.db.facade.FacadeExposed
import hu.bme.aut.examappbackend.dto.*
import kotlinx.coroutines.runBlocking
import java.util.*

class CorrectionService {

    suspend fun getQuestions(exam: String): List<Question>? {
        val questionString = FacadeExposed.examDao.getAllQuestionStringById(exam)
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
        var points: List<PointDto>
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
                val ans = answers[index][0].lowercase(Locale.getDefault())
                if(!(ans == "true".lowercase(Locale.getDefault()) || ans == "false".lowercase(Locale.getDefault()))){
                    throw IllegalArgumentException("Wrong input format")
                }
                pointsGained += if(question.correctAnswer.toString() == ans){
                    points.find { it.uuid == question.point }?.goodAnswer!!
                } else {
                    points.find { it.uuid == question.point }?.badAnswer!!
                }
                maxPoint += points.find { it.uuid == question.point }?.point!!
            }
            else if (question is MultipleChoiceQuestionDto){
                val badAnswers: MutableList<String> = mutableListOf()
                question.answers.forEach { if(!question.correctAnswersList.contains(it)) badAnswers.add(it) }
                for(answer in answers[index]){
                    var num = -1
                    try{
                        num = answer.toInt()
                        if(num < 0|| num >= question.answers.size ) throw IllegalArgumentException()
                    } catch (e: Exception){
                        throw IllegalArgumentException("Wrong input format")
                    }
                    if(question.correctAnswersList.contains(question.answers[num])){
                        pointsGained += points.find { it.uuid == question.point }?.goodAnswer!!
                    } else if(badAnswers.contains(question.answers[num])) {
                        pointsGained += points.find { it.uuid == question.point }?.badAnswer!!
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