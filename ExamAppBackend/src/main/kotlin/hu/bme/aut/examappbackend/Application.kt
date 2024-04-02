package hu.bme.aut.examappbackend

import hu.bme.aut.examappbackend.plugins.configureRouting
import hu.bme.aut.examappbackend.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}
