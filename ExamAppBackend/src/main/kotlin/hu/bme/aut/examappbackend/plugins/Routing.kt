package hu.bme.aut.examappbackend.plugins

import hu.bme.aut.examappbackend.db.facade.FacadeExposed
import hu.bme.aut.examappbackend.dto.ExamDto
import hu.bme.aut.examappbackend.routes.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put

fun Application.configureRouting() {
    routing {
        examRoutes()
        multipleChoiceRoutes()
        pointRoutes()
        topicRoutes()
        trueFalseRoutes()
        typeRoutes()
        //get("/") {
        //    call.respondText("Hello World!")
        //}
    }
}
