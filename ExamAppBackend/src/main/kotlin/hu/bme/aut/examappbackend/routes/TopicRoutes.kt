package hu.bme.aut.examappbackend.routes

import hu.bme.aut.examappbackend.db.facade.FacadeExposed
import hu.bme.aut.examappbackend.dto.TopicDto
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.Route

@Resource("/topic")
class TopicRoutes{
    @Resource("{topicId}")
    class TopicUUID(
        val topicParent: TopicRoutes = TopicRoutes(),
        val topicId: String
    )

    @Resource("name")
    class TopicNameList(
        val topicParent: TopicRoutes = TopicRoutes(),
    ) {

        @Resource("{topic}")
        class TopicName(
            val topicParent: TopicNameList = TopicNameList(),
            val topic: String
        )
    }
}

fun Route.topicRoutes(){
    get<TopicRoutes> {
        val topics = FacadeExposed.topicDao.getAllTopic()
        call.respond(status = HttpStatusCode.OK, message = topics)
    }

    get<TopicRoutes.TopicNameList> {
        val topics = FacadeExposed.topicDao.getAllTopicName()
        call.respond(status = HttpStatusCode.OK, message = topics)
    }

    get<TopicRoutes.TopicUUID> {
        val topic = FacadeExposed.topicDao.getTopicById(it.topicId)

        if(topic == null){
            call.respond(status = HttpStatusCode.BadRequest, message = "")
        } else {
            call.respond(status = HttpStatusCode.OK, message = topic)
        }
    }

    get<TopicRoutes.TopicNameList.TopicName> {
        val topic = FacadeExposed.topicDao.getTopicByTopic(it.topic)
            ?: return@get call.respond(status = HttpStatusCode.BadRequest, message = "")
        call.respond(status = HttpStatusCode.OK, message = topic)
    }

    put<TopicRoutes> {
        val topic = call.receive<TopicDto>()
        if(FacadeExposed.topicDao.updateTopic(topic)){
            call.respond(status = HttpStatusCode.OK, message = "")
        }
        else{
            call.respond(status = HttpStatusCode.BadRequest, message = "")
        }
    }

    post<TopicRoutes> {
        val topic = call.receive<TopicDto>()
        val created = FacadeExposed.topicDao.insertTopic(topic)
            ?: return@post call.respond(status = HttpStatusCode.InternalServerError, message = "")
        call.respond(status = HttpStatusCode.Created, message = created)
    }

    delete<TopicRoutes.TopicUUID> {
        if(FacadeExposed.topicDao.deleteTopic(it.topicId)){
            call.respond(status = HttpStatusCode.NoContent, message = "")
        } else {
            call.respond(status = HttpStatusCode.BadRequest, message = "")
        }
    }
}