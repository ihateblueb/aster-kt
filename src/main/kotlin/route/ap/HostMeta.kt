package me.blueb.route.ap

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import me.blueb.model.Configuration
import me.blueb.model.WellKnown
import me.blueb.model.WellKnownLink

fun Route.hostMeta() {
    val configuration = Configuration()

    get("/.well-known/host-meta") {
        call.response.headers.append("Content-Type", "application/jrd+json")

        call.respond(
            status = HttpStatusCode.OK,
            message = WellKnown(
                links = listOf(
                    WellKnownLink(
                        rel = "lrdd",
                        type = "application/jrd+json",
                        href = configuration.url.toString() + ".well-known/webfinger?resource={uri}",
                    )
                )
            )
        )
    }
}
