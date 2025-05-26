package me.blueb.route.api.mod

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.callid.callId
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.util.getOrFail
import kotlinx.serialization.Serializable
import me.blueb.db.table.PolicyTable
import me.blueb.model.ApiError
import me.blueb.model.PolicyType
import me.blueb.service.PolicyService
import me.blueb.service.TimelineService
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less

private val policyService = PolicyService()
private val timelineService = TimelineService()

@Serializable
data class PolicyBody(
	val type: PolicyType,
	val host: String? = null,
	val content: String? = null
)

fun Route.modPolicy() {
	authenticate("authRequiredMod") {
		get("/api/mod/policies") {
			val since = timelineService.normalizeSince(call.parameters["since"])
			val take = timelineService.normalizeTake(call.parameters["take"]?.toIntOrNull())

			val policies = policyService.getMany(PolicyTable.createdAt less since, take)

			if (policies.isEmpty()) {
				call.respond(HttpStatusCode.NoContent)
				return@get
			}

			call.respond(policies)
		}

		post("/api/mod/policy") {
			val body = call.receive<PolicyBody>()

			if (
				(body.type == PolicyType.Block || body.type == PolicyType.Silence || body.type == PolicyType.ForceSensitive || body.type == PolicyType.ForceContentWarning || body.type == PolicyType.ForceFollowRequest) && body.host == null
			) {
				call.respond(
					HttpStatusCode.BadRequest, 
					ApiError(
						"This type of policy requires a host",
						call.callId
					)
				)
				return@post
			}

			if (
				(body.type == PolicyType.ForceContentWarning) && body.content == null
			) {
				call.respond(
					HttpStatusCode.BadRequest,
					ApiError(
						"This type of policy requires content",
						call.callId
					)
				)
				return@post
			}

			call.respond(HttpStatusCode.NotImplemented)
		}

		patch("/api/mod/policy/{id}") {
			val policy = policyService.getById(call.parameters.getOrFail("id"))

			if (policy == null) {
				call.respond(HttpStatusCode.NotFound)
				return@patch
			}

			call.respond(HttpStatusCode.NotImplemented)
		}

		delete("/api/mod/policy/{id}") {
			val policy = policyService.getById(call.parameters.getOrFail("id"))

			if (policy == null) {
				call.respond(HttpStatusCode.NotFound)
				return@delete
			}

			policy.delete()

			call.respond(HttpStatusCode.OK)
		}
	}
}
