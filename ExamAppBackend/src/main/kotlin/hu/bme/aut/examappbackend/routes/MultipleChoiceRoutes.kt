package hu.bme.aut.examappbackend.routes

import hu.bme.aut.examappbackend.db.facade.FacadeExposed
import hu.bme.aut.examappbackend.dto.MultipleChoiceQuestionDto
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.Route

@Resource("/multipleChoice")
class MultipleChoiceRoutes{
    @Resource("{multipleChoiceId}")
    class MultipleChoiceUUID(
        val multipleChoiceParent: MultipleChoiceRoutes = MultipleChoiceRoutes(),
        val multipleChoiceId: String,
    )

    @Resource("name")
    class MultipleChoiceNameList(
        val multipleChoiceParent: MultipleChoiceRoutes = MultipleChoiceRoutes(),
    )
}

fun Route.multipleChoiceRoutes(){
    get<MultipleChoiceRoutes> {
        val questions = FacadeExposed.multipleChoiceQuestionDao.getAllMultipleChoiceQuestion()
        call.respond(status = HttpStatusCode.OK, message = questions)
    }

    get<MultipleChoiceRoutes.MultipleChoiceNameList> {
        val questions = FacadeExposed.multipleChoiceQuestionDao.getAllMultipleChoiceQuestionQuestion()
        call.respond(status = HttpStatusCode.OK, message = questions)
    }

    get<MultipleChoiceRoutes.MultipleChoiceUUID> {
        val question = FacadeExposed.multipleChoiceQuestionDao.getMultipleChoiceQuestionById(it.multipleChoiceId)

        if(question == null){
            call.respond(status = HttpStatusCode.BadRequest, message = "")
        } else {
            call.respond(status = HttpStatusCode.OK, message = question)
        }
    }

    put<MultipleChoiceRoutes> {
        val question = call.receive<MultipleChoiceQuestionDto>()
        if(FacadeExposed.multipleChoiceQuestionDao.updateMultipleChoiceQuestion(question)){
            call.respond(status = HttpStatusCode.OK, message = "")
        }
        else{
            call.respond(status = HttpStatusCode.BadRequest, message = "")
        }
    }

    post<MultipleChoiceRoutes> {
        val question = call.receive<MultipleChoiceQuestionDto>()
        val created = FacadeExposed.multipleChoiceQuestionDao.insertMultipleChoiceQuestion(question)
        if(created == null){
            call.respond(status = HttpStatusCode.InternalServerError, message = "")
        }
        else{
            call.respond(status = HttpStatusCode.Created, message = created)
        }
    }

    delete<MultipleChoiceRoutes.MultipleChoiceUUID> {
        if(FacadeExposed.multipleChoiceQuestionDao.deleteMultipleChoiceQuestion(it.multipleChoiceId)){
            call.respond(status = HttpStatusCode.NoContent, message = "")
        } else {
            call.respond(status = HttpStatusCode.BadRequest, message = "")
        }
    }
}