package me.blueb.routes.ap

import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resource
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import me.blueb.models.Nodeinfo
import me.blueb.models.NodeinfoSoftare
import me.blueb.models.NodeinfoUsage
import me.blueb.models.NodeinfoUsageUsers

@Resource("/nodeinfo/{version}")
class NodeinfoResource(val version: String = "2.0")

fun Route.nodeinfo() {
    get<NodeinfoResource> { res ->
        val version = res.version

        if (version != "2.0" && version != "2.1") {
            call.respond(HttpStatusCode.Companion.BadRequest)
        }

        val asterVersion = application.environment.config.property("version").getString()

        val nodeinfo = Nodeinfo(
            version = version,
            software = NodeinfoSoftare(
                name = "aster",
                version = asterVersion
            ),
            protocols = listOf("activitypub"),
            openRegistrations = true,
            usage = NodeinfoUsage(
                users = NodeinfoUsageUsers(0),
                localPosts = 0
            )
        )

        call.respond(
            nodeinfo
        )
    }
}