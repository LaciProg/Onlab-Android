package hu.bme.aut.examappbackend.routes


import hu.bme.aut.examappbackend.db.facade.FacadeExposed
import hu.bme.aut.examappbackend.dto.UserDto
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.Route

@Resource("/user")
class UserRoutes{
    @Resource("{userId}")
    class UserUUID(
        val userParent: UserRoutes = UserRoutes(),
        val userId: String
    )

    @Resource("{userName}")
    class UserName(
        val userParent: UserRoutes = UserRoutes(),
        val userName: String
    )
}

fun Route.userRoutes(){
    get<UserRoutes> {
        val types = FacadeExposed.userDao.getAllUser()
        call.respond(status = HttpStatusCode.OK, message = types)
    }

    get<UserRoutes.UserName> {
        val user = FacadeExposed.userDao.getUserByName(it.userName)
            ?: return@get call.respond(status = HttpStatusCode.BadRequest, message = "")
        call.respond(status = HttpStatusCode.OK, message = user)
    }

    get<UserRoutes.UserUUID> {
        val user = FacadeExposed.userDao.getUserById(it.userId)
            ?: return@get call.respond(status = HttpStatusCode.BadRequest, message = "")

        if (user.name != extractPrincipalUsername(call))
            return@get call.respond(HttpStatusCode.NotFound)

        call.respond(status = HttpStatusCode.OK, message = user)
    }

    put<UserRoutes> {
        val user = call.receive<UserDto>()
        if(FacadeExposed.userDao.updateUser(user)){
            call.respond(status = HttpStatusCode.OK, message = "")
        }
        else{
            call.respond(status = HttpStatusCode.BadRequest, message = "")
        }
    }

    post<UserRoutes> {
        val user = call.receive<UserDto>()
        val created = FacadeExposed.userDao.insertUser(user)
            ?: return@post call.respond(status = HttpStatusCode.InternalServerError, message = "")
        call.respond(status = HttpStatusCode.Created, message = created)
    }

    delete<UserRoutes.UserUUID> {
        if(FacadeExposed.userDao.deleteUser(it.userId)){
            call.respond(status = HttpStatusCode.NoContent, message = "")
        } else {
            call.respond(status = HttpStatusCode.BadRequest, message = "")
        }
    }
}

private fun extractPrincipalUsername(call: ApplicationCall): String? =
    call.principal<JWTPrincipal>()
        ?.payload
        ?.getClaim("username")
        ?.asString()