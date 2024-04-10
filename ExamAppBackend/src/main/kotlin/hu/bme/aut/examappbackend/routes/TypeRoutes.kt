package hu.bme.aut.examappbackend.routes

import hu.bme.aut.examappbackend.db.facade.FacadeExposed
import hu.bme.aut.examappbackend.dto.TypeDto
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.Route

@Resource("/type")
class TypeRoutes{
    @Resource("{typeId}")
    class TypeUUID(
        val typeParent: TypeRoutes = TypeRoutes(),
        val typeId: String
    )

    @Resource("name")
    class TypeNameList(
        val typeParent: TypeRoutes = TypeRoutes(),
    )
}

fun Route.typeRoutes(){
    get<TypeRoutes> {
        val types = FacadeExposed.typeDao.getAllType()
        call.respond(status = HttpStatusCode.OK, message = types)
    }

    get<TypeRoutes.TypeNameList> {
        val types = FacadeExposed.typeDao.getAllTypeType()
        call.respond(status = HttpStatusCode.OK, message = types)
    }

    get<TypeRoutes.TypeUUID> {
        val type = FacadeExposed.typeDao.getTypeById(it.typeId)
            ?: return@get call.respond(status = HttpStatusCode.BadRequest, message = "")
        call.respond(status = HttpStatusCode.OK, message = type)
    }

    put<TypeRoutes> {
        val type = call.receive<TypeDto>()
        if(FacadeExposed.typeDao.updateType(type)){
            call.respond(status = HttpStatusCode.OK, message = "")
        }
        else{
            call.respond(status = HttpStatusCode.BadRequest, message = "")
        }
    }

    post<TypeRoutes> {
        val type = call.receive<TypeDto>()
        val created = FacadeExposed.typeDao.insertType(type)
            ?: return@post call.respond(status = HttpStatusCode.InternalServerError, message = "")
        call.respond(status = HttpStatusCode.Created, message = created)
    }

    delete<TypeRoutes.TypeUUID> {
        if(FacadeExposed.typeDao.deleteType(it.typeId)){
            call.respond(status = HttpStatusCode.NoContent, message = "")
        } else {
            call.respond(status = HttpStatusCode.BadRequest, message = "")
        }
    }
}