package hu.bme.aut.examappbackend.plugins

import hu.bme.aut.examappbackend.routes.*
import hu.bme.aut.examappbackend.services.CorrectionService
import hu.bme.aut.examappbackend.services.JwtService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(jwtService: JwtService) {
    val correctionService = CorrectionService()
    routing {

        route("/auth"){
            authRoute(jwtService)
        }
        userRoutes()

        authenticate {
            examRoutes()
            correctionRoute(correctionService)
            multipleChoiceRoutes()
            pointRoutes()
            topicRoutes()
            trueFalseRoutes()
            typeRoutes()
        }
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
