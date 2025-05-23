package me.blueb.route.api.admin

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import me.blueb.db.table.PolicyTable
import me.blueb.service.PolicyService
import me.blueb.service.TimelineService
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less

private val policyService = PolicyService()
private val timelineService = TimelineService()

fun Route.adminPolicy() {
	authenticate("authRequiredAdmin") {
		get("/api/admin/policies") {
			val since = LocalDateTime.parse(call.parameters["since"] ?: Clock.System.now().toString())
			var take = call.parameters["take"]?.toIntOrNull()

			take = timelineService.normalizeTake(take)

			val policies = policyService.getMany(PolicyTable.createdAt less since, take)

			if (policies.isEmpty()) {
				call.respond(HttpStatusCode.NoContent)
				return@get
			}

			call.respond(policies)
		}

		post("/api/admin/policy") {
			call.respond(HttpStatusCode.NotImplemented)
		}

		patch("/api/admin/policy/{id}") {
			call.respond(HttpStatusCode.NotImplemented)
		}

		delete("/api/admin/policy/{id}") {
			call.respond(HttpStatusCode.NotImplemented)
		}
	}
}
