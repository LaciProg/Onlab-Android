package hu.bme.aut.examappbackend.routes

import hu.bme.aut.examappbackend.dto.UserDto
import hu.bme.aut.examappbackend.services.JwtService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoute(jwtService: JwtService){

    post {
        val loginRequest = call.receive<UserDto>()

        val token = jwtService.createJwtToken(loginRequest)

        token?.let {
            call.respond(hashMapOf("token" to it))
        } ?: call.respond(HttpStatusCode.Unauthorized)
    }
}