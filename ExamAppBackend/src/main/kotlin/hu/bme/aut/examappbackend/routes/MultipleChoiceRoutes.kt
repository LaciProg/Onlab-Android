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
            ?: return@get call.respond(status = HttpStatusCode.BadRequest, message = "")
        call.respond(status = HttpStatusCode.OK, message = question)
    }

    put<MultipleChoiceRoutes> {
        val question = call.receive<MultipleChoiceQuestionDto>()
        val typeID = FacadeExposed.typeDao.getTypeByType(question.type)?.uuid ?: ""
        val questionWithTypeID = MultipleChoiceQuestionDto(
            uuid = question.uuid,
            question = question.question,
            answers = question.answers,
            correctAnswersList = question.correctAnswersList,
            point = question.point,
            topic = question.topic,
            type = typeID
        )
        if(FacadeExposed.multipleChoiceQuestionDao.updateMultipleChoiceQuestion(questionWithTypeID)){
            call.respond(status = HttpStatusCode.OK, message = "")
        }
        else{
            call.respond(status = HttpStatusCode.BadRequest, message = "")
        }
    }

    post<MultipleChoiceRoutes> {
        val question = call.receive<MultipleChoiceQuestionDto>()
        val typeID = FacadeExposed.typeDao.getTypeByType(question.type)?.uuid ?: ""
        val questionWithTypeID = MultipleChoiceQuestionDto(
            uuid = question.uuid,
            question = question.question,
            answers = question.answers,
            correctAnswersList = question.correctAnswersList,
            point = question.point,
            topic = question.topic,
            type = typeID
        )
        val created = FacadeExposed.multipleChoiceQuestionDao.insertMultipleChoiceQuestion(questionWithTypeID)
            ?: return@post call.respond(status = HttpStatusCode.InternalServerError, message = "")
        call.respond(status = HttpStatusCode.Created, message = created)
    }

    delete<MultipleChoiceRoutes.MultipleChoiceUUID> {
        if(FacadeExposed.multipleChoiceQuestionDao.deleteMultipleChoiceQuestion(it.multipleChoiceId)){
            call.respond(status = HttpStatusCode.NoContent, message = "")
        } else {
            call.respond(status = HttpStatusCode.BadRequest, message = "")
        }
    }
}