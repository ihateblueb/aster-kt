package site.remlit.blueb.aster.route.ap

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import site.remlit.blueb.aster.db.table.UserTable
import site.remlit.blueb.aster.model.ApiException
import site.remlit.blueb.aster.model.Configuration
import site.remlit.blueb.aster.model.WellKnown
import site.remlit.blueb.aster.model.WellKnownLink
import site.remlit.blueb.aster.route.RouteRegistry
import site.remlit.blueb.aster.service.UserService

object WebfingerRoutes {
	fun register() =
		RouteRegistry.registerRoute {
			get("/.well-known/webfinger") {
				val resource = call.queryParameters.getOrFail("resource")

				call.response.headers.append("Content-Type", "application/activity+json")

				if (!resource.startsWith("acct:") || resource == "acct:")
					throw ApiException(HttpStatusCode.BadRequest, "Only 'acct' resource type supported")

				val username = resource
					.replace("acct:@", "")
					.replace("acct:", "")
					.replace("@${Configuration.url.host}", "")
					.replace("@", "")

				val user = UserService.get(
					UserTable.username eq username
							and (UserTable.host eq null)
				)

				if (user == null || user.suspended || !user.activated)
					throw ApiException(HttpStatusCode.NotFound)

				call.respond(
					status = HttpStatusCode.OK,
					message = WellKnown(
						subject = "$resource@${Configuration.url.host}",
						aliases = listOf(
							user.apId,
							Configuration.url.toString() + "users/" + user.username,
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
								href = Configuration.url.toString() + "@" + user.username,
							)
						)
					)
				)
			}
		}
}