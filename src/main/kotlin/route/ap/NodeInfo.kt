package site.remlit.blueb.aster.route.ap

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.neq
import org.jetbrains.exposed.sql.and
import site.remlit.blueb.aster.db.table.UserTable
import site.remlit.blueb.aster.model.*
import site.remlit.blueb.aster.service.NoteService
import site.remlit.blueb.aster.service.UserService

fun Route.nodeInfo() {
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
						name = packageInformation.name,
						version = packageInformation.version,
					),
				protocols = listOf("activitypub"),
				openRegistrations = configuration.registrations == InstanceRegistrationsType.Open,
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
