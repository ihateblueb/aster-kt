package me.blueb.route.ap

import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resource
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import me.blueb.model.Configuration
import me.blueb.model.Nodeinfo
import me.blueb.model.NodeinfoSoftare
import me.blueb.model.NodeinfoUsage
import me.blueb.model.NodeinfoUsageUsers
import me.blueb.model.repository.UserRepository

@Resource("/nodeinfo/{version}")
class NodeinfoResource(
    val version: String = "2.0",
)

fun Route.nodeinfo() {
    val configuration = Configuration()

    get<NodeinfoResource> { res ->
        val version = res.version

        if (version != "2.0" && version != "2.1") {
            call.respond(HttpStatusCode.Companion.BadRequest)
        }

        val nodeinfo =
            Nodeinfo(
                version = version,
                software =
                    NodeinfoSoftare(
                        name = configuration.software.name,
                        version = configuration.software.version,
                    ),
                protocols = listOf("activitypub"),
                openRegistrations = true,
                usage =
                    NodeinfoUsage(
                        users = NodeinfoUsageUsers(0),
                        localPosts = 0,
                    ),
            )

        call.respond(
            nodeinfo,
        )
    }
}
