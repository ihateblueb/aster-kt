package me.blueb.route.ap

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.util.getOrFail
import me.blueb.db.table.UserTable
import me.blueb.model.Configuration
import me.blueb.model.WellKnown
import me.blueb.model.WellKnownLink
import me.blueb.service.UserService
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and

fun Route.webfinger() {
    val configuration = Configuration()
    val userService = UserService()

    get("/.well-known/webfinger") {
        val resource = call.queryParameters.getOrFail("resource")

        call.response.headers.append("Content-Type", "application/activity+json")

        if (!resource.startsWith("acct:") || resource == "acct:") {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }

        val username = resource
            .replace("acct:@", "")
            .replace("acct:", "")
            .replace("@${configuration.url.host}", "")
            .replace("@", "")

        val user = userService.get(
            listOf(
                UserTable.username eq username,
                UserTable.host eq null
            )
        )

        if (user == null || user.suspended || !user.activated) {
            call.respond(HttpStatusCode.NotFound)
            return@get
        }

        call.respond(
            status = HttpStatusCode.OK,
            message = WellKnown(
                subject = resource,
                aliases = listOf(
                    user.apId,
                    configuration.url.toString() + "users/" + user.username,
                ),
                links = listOf(
                    WellKnownLink(
                        rel = "self",
                        type = "application/activity+json",
                        href = user.apId,
                    ),
                    WellKnownLink(
                        rel = "http://webfinger.net/rel/profile-page",
                        type = "text/html",
                        href = configuration.url.toString() + "@" + user.username,
                    )
                )
            )
        )
    }
}