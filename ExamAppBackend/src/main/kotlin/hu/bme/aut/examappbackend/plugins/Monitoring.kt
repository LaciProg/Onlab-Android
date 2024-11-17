package hu.bme.aut.examappbackend.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*  // Helyes csomagnév
import io.ktor.server.request.*
import org.slf4j.event.Level

fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.DEBUG
        filter { call -> call.request.path().startsWith("/") }
    }
}