package site.remlit.blueb.aster.route.api.mod

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.core.less
import site.remlit.blueb.aster.db.entity.PolicyEntity
import site.remlit.blueb.aster.db.suspendTransaction
import site.remlit.blueb.aster.db.table.PolicyTable
import site.remlit.blueb.aster.model.ApiException
import site.remlit.blueb.aster.model.Policy
import site.remlit.blueb.aster.model.PolicyType
import site.remlit.blueb.aster.route.RouteRegistry
import site.remlit.blueb.aster.service.IdentifierService
import site.remlit.blueb.aster.service.PolicyService
import site.remlit.blueb.aster.service.TimelineService

object PolicyRoutes {
	@Serializable
	data class PolicyBody(
		val type: PolicyType,
		val host: String,
		val content: String? = null
	)

	fun register() =
		RouteRegistry.registerRoute {
			authenticate("authRequiredMod") {
				get("/api/mod/policies") {
					val since = TimelineService.normalizeSince(call.parameters["since"])
					val take = TimelineService.normalizeTake(call.parameters["take"]?.toIntOrNull())

					val policies = PolicyService.getMany(PolicyTable.createdAt less since, take)

					if (policies.isEmpty()) {
						call.respond(HttpStatusCode.NoContent)
						return@get
					}

					call.respond(Policy.fromEntities(policies))
				}

				post("/api/mod/policy") {
					val body = call.receive<PolicyBody>()

					if (
						(body.type == PolicyType.ForceContentWarning) && body.content == null
					)
						throw ApiException(HttpStatusCode.BadRequest, "This policy type requires content")

					val id = IdentifierService.generate()

					suspendTransaction {
						PolicyEntity.new(id) {
							type = body.type
							host = body.host
							content = body.content
						}
					}

					val policy = PolicyService.getById(id) ?: throw ApiException(
						HttpStatusCode.NotFound,
						"Policy doesn't exist"
					)

					call.respond(Policy.fromEntity(policy))
				}

				patch("/api/mod/policy/{id}") {
					val policy =
						PolicyService.getById(call.parameters.getOrFail("id"))
							?: throw ApiException(HttpStatusCode.NotFound)

					throw ApiException(HttpStatusCode.NotImplemented)
				}

				delete("/api/mod/policy/{id}") {
					val policy =
						PolicyService.getById(call.parameters.getOrFail("id"))
							?: throw ApiException(HttpStatusCode.NotFound)

					suspendTransaction {
						policy.delete()
					}

					call.respond(HttpStatusCode.OK)
				}
			}
		}
}
