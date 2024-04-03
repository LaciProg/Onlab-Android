package hu.bme.aut.examappbackend.plugins

import hu.bme.aut.examappbackend.routes.typeRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        typeRoutes()
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
