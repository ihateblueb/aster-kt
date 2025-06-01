package site.remlit.blueb.route.api.mod

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import site.remlit.blueb.db.table.PolicyTable
import site.remlit.blueb.model.ApiError
import site.remlit.blueb.model.PolicyType
import site.remlit.blueb.service.PolicyService
import site.remlit.blueb.service.TimelineService

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
