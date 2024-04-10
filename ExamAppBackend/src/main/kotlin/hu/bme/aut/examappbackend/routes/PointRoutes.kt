package hu.bme.aut.examappbackend.routes

import hu.bme.aut.examappbackend.db.facade.FacadeExposed
import hu.bme.aut.examappbackend.dto.PointDto
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.Route

@Resource("/point")
class PointRoutes{
    @Resource("{pointId}")
    class PointUUID(
        val pointParent: PointRoutes = PointRoutes(),
        val pointId: String
    )

    @Resource("name")
    class PontNameList(
        val pointParent: PointRoutes = PointRoutes(),
    )
}

fun Route.pointRoutes(){
    get<PointRoutes> {
        val points = FacadeExposed.pointDao.getAllPoint()
        call.respond(status = HttpStatusCode.OK, message = points)
    }

    get<PointRoutes.PontNameList> {
        val points = FacadeExposed.pointDao.getAllPointType()
        call.respond(status = HttpStatusCode.OK, message = points)
    }

    get<PointRoutes.PointUUID> {
        val point = FacadeExposed.pointDao.getPointById(it.pointId)

        if(point == null) {
            call.respond(status = HttpStatusCode.BadRequest, message = "")
        } else {
            call.respond(status = HttpStatusCode.OK, message = point)
        }
    }

    put<PointRoutes> {
        val point = call.receive<PointDto>()
        if(FacadeExposed.pointDao.updatePoint(point)){
            call.respond(status = HttpStatusCode.OK, message = "")
        }
        else{
            call.respond(status = HttpStatusCode.BadRequest, message = "")
        }
    }

    post<PointRoutes> {
        val point = call.receive<PointDto>()
        val created = FacadeExposed.pointDao.insertPoint(point)
        if(created == null){
            call.respond(status = HttpStatusCode.InternalServerError, message = "")
        }
        else{
            call.respond(status = HttpStatusCode.Created, message = created)
        }
    }

    delete<PointRoutes.PointUUID> {
        if(FacadeExposed.pointDao.deletePoint(it.pointId)){
            call.respond(status = HttpStatusCode.NoContent, message = "")
        } else {
            call.respond(status = HttpStatusCode.BadRequest, message = "")
        }
    }
}