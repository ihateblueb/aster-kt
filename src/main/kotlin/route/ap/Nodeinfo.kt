package me.blueb.route.ap

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.util.getOrFail
import me.blueb.model.Configuration
import me.blueb.model.Nodeinfo
import me.blueb.model.NodeinfoSoftare
import me.blueb.model.NodeinfoUsage
import me.blueb.model.NodeinfoUsageUsers
import me.blueb.model.PackageInformation
import me.blueb.model.WellKnown
import me.blueb.model.WellKnownLink

fun Route.nodeinfo() {
    val configuration = Configuration()
	val packageInformation = PackageInformation()

    get("/.well-known/nodeinfo") {
        call.response.headers.append("Content-Type", "application/jrd+json")

        call.respond(
            status = HttpStatusCode.OK,
            message = WellKnown(
                links = listOf(
                    WellKnownLink(
                        rel = "http://nodeinfo.diaspora.software/ns/schema/2.1",
                        href = configuration.url.toString() + "nodeinfo/2.1",
                    ),
                    WellKnownLink(
                        rel = "http://nodeinfo.diaspora.software/ns/schema/2.0",
                        href = configuration.url.toString() + "nodeinfo/2.0",
                    )
                )
            )
        )
    }

    get("/nodeinfo/{version}") {
        val version = call.parameters.getOrFail("version")

        if (version != "2.0" && version != "2.1") {
            call.respond(HttpStatusCode.BadRequest)
        }

        val nodeinfo =
            Nodeinfo(
                version = version,
                software =
                    NodeinfoSoftare(
                        name = packageInformation.name,
                        version = packageInformation.version,
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
