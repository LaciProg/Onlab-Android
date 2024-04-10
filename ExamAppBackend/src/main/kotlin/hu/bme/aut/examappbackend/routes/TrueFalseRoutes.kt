package hu.bme.aut.examappbackend.routes

import hu.bme.aut.examappbackend.db.facade.FacadeExposed
import hu.bme.aut.examappbackend.dto.TrueFalseQuestionDto
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.Route

@Resource("/trueFalse")
class TrueFalseRoutes{
    @Resource("{trueFalseId}")
    class TrueFalseUUID(
        val trueFalseParent: TrueFalseRoutes = TrueFalseRoutes(),
        val trueFalseId: String
    )

    @Resource("name")
    class TrueFalseNameList(
        val trueFalseParent: TrueFalseRoutes = TrueFalseRoutes(),
    )
}

fun Route.trueFalseRoutes(){
    get<TrueFalseRoutes> {
        val questions = FacadeExposed.trueFalseQuestionDao.getAllTrueFalseQuestion()
        call.respond(status = HttpStatusCode.OK, message = questions)
    }

    get<TrueFalseRoutes.TrueFalseNameList> {
        val questions = FacadeExposed.trueFalseQuestionDao.getAllTrueFalseQuestionQuestion()
        call.respond(status = HttpStatusCode.OK, message = questions)
    }

    get<TrueFalseRoutes.TrueFalseUUID> {
        val question = FacadeExposed.trueFalseQuestionDao.getTrueFalseQuestionById(it.trueFalseId)

        if(question == null){
            call.respond(status = HttpStatusCode.BadRequest, message = "")
        } else {
            call.respond(status = HttpStatusCode.OK, message = question)
        }
    }

    put<TrueFalseRoutes> {
        val question = call.receive<TrueFalseQuestionDto>()
        if(FacadeExposed.trueFalseQuestionDao.updateTrueFalseQuestion(question)){
            call.respond(status = HttpStatusCode.OK, message = "")
        }
        else{
            call.respond(status = HttpStatusCode.BadRequest, message = "")
        }
    }

    post<TrueFalseRoutes> {
        val question = call.receive<TrueFalseQuestionDto>()
        val created = FacadeExposed.trueFalseQuestionDao.insertTrueFalseQuestion(question)
        if(created == null){
            call.respond(status = HttpStatusCode.InternalServerError, message = "")
        }
        else{
            call.respond(status = HttpStatusCode.Created, message = created)
        }
    }

    delete<TrueFalseRoutes.TrueFalseUUID> {
        if(FacadeExposed.trueFalseQuestionDao.deleteTrueFalseQuestion(it.trueFalseId)){
            call.respond(status = HttpStatusCode.NoContent, message = "")
        } else {
            call.respond(status = HttpStatusCode.BadRequest, message = "")
        }
    }
}