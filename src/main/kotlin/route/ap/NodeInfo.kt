package site.remlit.blueb.aster.route.ap

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.neq
import site.remlit.blueb.aster.db.table.UserTable
import site.remlit.blueb.aster.model.ApiException
import site.remlit.blueb.aster.model.Configuration
import site.remlit.blueb.aster.model.InstanceRegistrationsType
import site.remlit.blueb.aster.model.NodeInfo
import site.remlit.blueb.aster.model.NodeInfoSoftware
import site.remlit.blueb.aster.model.NodeInfoUsage
import site.remlit.blueb.aster.model.NodeInfoUsageUsers
import site.remlit.blueb.aster.model.PackageInformation
import site.remlit.blueb.aster.model.WellKnown
import site.remlit.blueb.aster.model.WellKnownLink
import site.remlit.blueb.aster.service.NoteService
import site.remlit.blueb.aster.service.UserService

fun Route.nodeInfo() {
	get("/.well-known/nodeinfo") {
		call.response.headers.append("Content-Type", "application/jrd+json")

		call.respond(
			status = HttpStatusCode.OK,
			message = WellKnown(
				links = listOf(
					WellKnownLink(
						rel = "http://nodeinfo.diaspora.software/ns/schema/2.1",
						href = Configuration.url.toString() + "nodeinfo/2.1",
					),
					WellKnownLink(
						rel = "http://nodeinfo.diaspora.software/ns/schema/2.0",
						href = Configuration.url.toString() + "nodeinfo/2.0",
					)
				)
			)
		)
	}

	get("/nodeinfo/{version}") {
		val version = call.parameters.getOrFail("version")

		if (version != "2.0" && version != "2.1")
			throw ApiException(HttpStatusCode.BadRequest, "Invalid version")

		val userCount = UserService.count(
			UserTable.host eq null
					and (UserTable.username neq "instance.actor")
		).toInt()

		val noteCount = NoteService.count(
			UserTable.host eq null
					and (UserTable.username neq "instance.actor")
		).toInt()

		val nodeInfo =
			NodeInfo(
				version = version,
				software =
					NodeInfoSoftware(
						name = PackageInformation.name,
						version = PackageInformation.version,
					),
				protocols = listOf("activitypub"),
				openRegistrations = Configuration.registrations == InstanceRegistrationsType.Open,
				usage =
					NodeInfoUsage(
						users = NodeInfoUsageUsers(userCount),
						localPosts = noteCount,
					),
			)

		call.respond(
			nodeInfo,
		)
	}
}
