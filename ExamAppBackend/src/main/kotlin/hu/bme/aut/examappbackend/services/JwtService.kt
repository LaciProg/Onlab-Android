package hu.bme.aut.examappbackend.services

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import hu.bme.aut.examappbackend.db.facade.FacadeExposed
import hu.bme.aut.examappbackend.dto.UserDto
import io.ktor.server.application.*
import io.ktor.server.auth.jwt.*
import java.util.*

class JwtService(private val application: Application, ) {
    private val secret = getConfigProperty("jwt.secret")
    private val issuer = getConfigProperty("jwt.issuer")
    private val audience = getConfigProperty("jwt.audience")
    val realm = getConfigProperty("jwt.realm")

    val jwtVerifier: JWTVerifier =
        JWT
            .require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build()

    suspend fun createJwtToken(loginRequest: UserDto): String? {
        val foundUser: UserDto? =   FacadeExposed.userDao.getUserByName(loginRequest.name)
        return if (foundUser != null && loginRequest.password == foundUser.password)
            JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("username", loginRequest.name)
                .withExpiresAt(Date(System.currentTimeMillis() + 3_600_000))
                .sign(Algorithm.HMAC256(secret))
        else
            null
    }

    suspend fun customValidator(
        credential: JWTCredential,
    ): JWTPrincipal? {
        val username: String = extractUsername(credential) ?: return null
        val foundUser: UserDto? =   FacadeExposed.userDao.getUserByName(username)
        return foundUser?.let {
            if (audienceMatches(credential))
                JWTPrincipal(credential.payload)
            else
                null
        }
    }

    private fun audienceMatches(credential: JWTCredential): Boolean =
        credential.payload.audience.contains(audience)

    private fun getConfigProperty(path: String) =
        application.environment.config.property(path).getString()

    private fun extractUsername(credential: JWTCredential): String? =
        credential.payload.getClaim("username").asString()
}