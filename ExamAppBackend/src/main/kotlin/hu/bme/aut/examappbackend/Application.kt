package hu.bme.aut.examappbackend

import hu.bme.aut.examappbackend.db.DatabaseFactory
import hu.bme.aut.examappbackend.plugins.*
import hu.bme.aut.examappbackend.services.JwtService
import io.ktor.server.application.*
import io.ktor.server.resources.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val jwtService = JwtService(this)
    DatabaseFactory.init()
    DatabaseFactory.createSampleData()
    install(Resources)
    configureHTTP()
    configureMonitoring()
    configureSecurity(jwtService)
    configureSerialization()
    configureRouting(jwtService)
}
