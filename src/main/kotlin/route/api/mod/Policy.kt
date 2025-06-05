package site.remlit.blueb.aster.route.api.mod

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import site.remlit.blueb.aster.db.table.PolicyTable
import site.remlit.blueb.aster.model.ApiException
import site.remlit.blueb.aster.model.PolicyType
import site.remlit.blueb.aster.service.PolicyService
import site.remlit.blueb.aster.service.TimelineService

@Serializable
data class PolicyBody(
	val type: PolicyType,
	val host: String? = null,
	val content: String? = null
)

fun Route.modPolicy() {
	val policyService = PolicyService()
	val timelineService = TimelineService()

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
			)
				throw ApiException(HttpStatusCode.BadRequest, "This policy type requires a host")

			if (
				(body.type == PolicyType.ForceContentWarning) && body.content == null
			)
				throw ApiException(HttpStatusCode.BadRequest, "This policy type requires content")

			throw ApiException(HttpStatusCode.NotImplemented)
		}

		patch("/api/mod/policy/{id}") {
			val policy = policyService.getById(call.parameters.getOrFail("id"))

			if (policy == null)
				throw ApiException(HttpStatusCode.NotFound)

			throw ApiException(HttpStatusCode.NotImplemented)
		}

		delete("/api/mod/policy/{id}") {
			val policy = policyService.getById(call.parameters.getOrFail("id"))

			if (policy == null)
				throw ApiException(HttpStatusCode.NotFound)

			policy.delete()

			call.respond(HttpStatusCode.OK)
		}
	}
}
