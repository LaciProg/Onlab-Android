package hu.bme.aut.examappbackend.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
fun Application.configureJwt() {
    val conf = environment.config

    val audience = conf.property("jwt.audience").getString()
    var realm = conf.property("jwt.realm").getString()
    val privateKeyString  = conf.property("jwt.privateKey ").getString()
    val issuer = conf.property("jwt.issuer").getString()

    install(Authentication) {
        jwt {
            // Configure jwt authentication
        }
    }
}