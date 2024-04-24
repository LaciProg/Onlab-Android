package hu.bme.aut.examappbackend.routes

import enums.Type
import hu.bme.aut.examappbackend.db.facade.FacadeExposed
import hu.bme.aut.examappbackend.dto.ExamDto
import hu.bme.aut.examappbackend.dto.Question
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.Route

@Resource("/exam")
class ExamRoutes{
    @Resource("/{examId}")
    class ExamUUID(
        val examParent: ExamRoutes = ExamRoutes(),
        val examId: String
    ){
        @Resource("/question")
        class QuestionList(
            val examParent: ExamRoutes = ExamRoutes(),
        )
    }

    @Resource("/name")
    class ExamList(
        val examParent: ExamRoutes = ExamRoutes(),
    )


}

fun Route.examRoutes(){
    get<ExamRoutes> {
        val exams = FacadeExposed.examDao.getAllExam()
        call.respond(status = HttpStatusCode.OK, message = exams)
    }

    get<ExamRoutes.ExamList> {
        val exams = FacadeExposed.examDao.getAllExamNames()
        call.respond(status = HttpStatusCode.OK, message = exams)
    }

    get<ExamRoutes.ExamUUID.QuestionList> {
        /*val questions: MutableList<Question> = mutableListOf()
        val examIds = FacadeExposed.examDao.getAllExam().map { it.uuid }
        val questionStrings: HashSet<String> = hashSetOf()
        examIds.forEach { FacadeExposed.examDao.getAllQuestionString(it)?.let { it1 -> questionStrings.add(it1) } }
        val usedQuestions: MutableList<String> = mutableListOf()
        questionStrings.forEach { usedQuestions.addAll(it.split("#")) }

        usedQuestions.forEach {
            when(it.substringBefore("~").toInt()){
                Type.trueFalseQuestion.ordinal -> {
                    val q = FacadeExposed.trueFalseQuestionDao.getTrueFalseQuestionById(it.substringAfter("~"))
                    if(q != null){ questions.add(q) }
                }
                Type.multipleChoiceQuestion.ordinal -> {
                    val q = FacadeExposed.multipleChoiceQuestionDao.getMultipleChoiceQuestionById(it.substringAfter("~"))
                    if(q != null){ questions.add(q) }
                }
            }
        }*/
        val questions = FacadeExposed.examDao.getAllQuestion()
        //questions.addAll(FacadeExposed.multipleChoiceQuestionDao.getAllMultipleChoiceQuestion())
        //questions.addAll(FacadeExposed.trueFalseQuestionDao.getAllTrueFalseQuestion())
        call.respond(status = HttpStatusCode.OK, message = questions)
    }

    get<ExamRoutes.ExamUUID> {
        val exam = FacadeExposed.examDao.getExamById(it.examId)
            ?: return@get call.respond(status = HttpStatusCode.BadRequest, message = "")
        call.respond(status = HttpStatusCode.OK, message = exam)
    }

    put<ExamRoutes> {
        val exam = call.receive<ExamDto>()
        if(FacadeExposed.examDao.updateExam(exam)){
            call.respond(status = HttpStatusCode.OK, message = "")
        }
        else{
            call.respond(status = HttpStatusCode.BadRequest, message = "")
        }
    }

    post<ExamRoutes> {
        val exam = call.receive<ExamDto>()
        val created = FacadeExposed.examDao.insertExam(exam)
            ?: return@post call.respond(status = HttpStatusCode.InternalServerError, message = "")
        call.respond(status = HttpStatusCode.Created, message = created)
    }

    delete<ExamRoutes.ExamUUID> {
        if(FacadeExposed.examDao.deleteExam(it.examId)){
            call.respond(status = HttpStatusCode.NoContent, message = "")
        } else {
            call.respond(status = HttpStatusCode.BadRequest, message = "")
        }
    }
}