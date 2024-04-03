package hu.bme.aut.examappbackend

import hu.bme.aut.examappbackend.db.DatabaseFactory
import hu.bme.aut.examappbackend.plugins.configureHTTP
import hu.bme.aut.examappbackend.plugins.configureMonitoring
import hu.bme.aut.examappbackend.plugins.configureRouting
import hu.bme.aut.examappbackend.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.resources.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseFactory.init()
    install(Resources)
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureRouting()
}
