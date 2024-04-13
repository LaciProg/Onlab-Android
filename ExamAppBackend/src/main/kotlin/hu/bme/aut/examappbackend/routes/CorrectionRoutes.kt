package hu.bme.aut.examappbackend.routes

import hu.bme.aut.examappbackend.services.CorrectionService
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

@Resource("/correction")
class CorrectionRoutes{
    @Resource("{exam}")
    class Exam(
        val examParent: CorrectionRoutes = CorrectionRoutes(),
        val exam: String = ""
    ) {
        @Resource("/{answers}")
        class Correction(
            val examParent: Exam = Exam(CorrectionRoutes()),
            val answers: String
        )
    }
}

fun Route.correctionRoute(correctionService: CorrectionService){
    get<CorrectionRoutes.Exam>{
        val questions = correctionService.getQuestions(exam = it.exam)
            ?: return@get call.respond(status = HttpStatusCode.BadRequest, message = "")
        call.respond(status = HttpStatusCode.OK, message = questions)
    }

    get<CorrectionRoutes.Exam.Correction> {
        val answersList = it.answers.split("-")
        val answers: MutableList<List<String>> = mutableListOf()
        answersList.forEach { answerString -> answers.add(answerString.split(",")) }
        val result = correctionService.correcting(it.examParent.exam, answers)
            ?: return@get call.respond(status = HttpStatusCode.BadRequest, message = "")
        call.respond(status = HttpStatusCode.OK, message = result)
    }
}