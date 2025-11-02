package site.remlit.aster.service

import kotlinx.serialization.json.JsonObject
import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.slf4j.LoggerFactory
import site.remlit.aster.common.model.generated.PartialInstance
import site.remlit.aster.db.entity.InstanceEntity
import site.remlit.aster.db.table.InstanceTable
import site.remlit.aster.model.Service
import site.remlit.aster.service.ap.ApUtilityService

class InstanceService : Service() {
	companion object {
		private val logger = LoggerFactory.getLogger(InstanceService::class.java)

		const val NODEINFO_2_0: String = "http://nodeinfo.diaspora.software/ns/schema/2.0"
		const val NODEINFO_2_1: String = "http://nodeinfo.diaspora.software/ns/schema/2.1"

		fun get(where: Op<Boolean>): InstanceEntity? = transaction {
			InstanceEntity
				.find { where }
				.singleOrNull()
		}

		fun getById(id: String): InstanceEntity? = get(InstanceTable.id eq id)
		fun getByHost(host: String): InstanceEntity? = get(InstanceTable.host eq host)

		suspend fun resolve(host: String, refetch: Boolean = false): InstanceEntity? {
			val existing = get(InstanceTable.host eq host)

			if ((existing != null) && !refetch) return existing

			val wellknownNodeinfoResponse = ResolverService.resolveSigned("https://$host/.well-known/nodeinfo")
				?: return null

			val links = ApUtilityService.extractArray(wellknownNodeinfoResponse["links"]) ?: return null
			var nodeinfoHref = ""

			for (link in links) {
				link as JsonObject

				val rel = ApUtilityService.extractString(link["rel"]) ?: continue
				val href = ApUtilityService.extractString(link["href"]) ?: continue

				if (rel == NODEINFO_2_0 && nodeinfoHref.isBlank() || rel == NODEINFO_2_1)
					nodeinfoHref = href
			}

			val resolvedNodeinfo = ResolverService.resolveSigned(nodeinfoHref)

			if (resolvedNodeinfo != null && existing == null)
				return register(toInstance(host, resolvedNodeinfo, existing))

			if (resolvedNodeinfo != null && existing != null)
				return update(toInstance(host, resolvedNodeinfo, existing))

			return null
		}

		fun toInstance(host: String, nodeinfo: JsonObject, existing: InstanceEntity? = null): PartialInstance {
			val id = existing?.id?.toString() ?: IdentifierService.generate()

			val metadata = ApUtilityService.extractObject(nodeinfo["metadata"])

			val name = ApUtilityService.extractString(metadata?.get("nodeName"))
			val description = ApUtilityService.extractString(metadata?.get("nodeDescription"))
			val color = ApUtilityService.extractString(metadata?.get("themeColor"))

			val maintainer = ApUtilityService.extractObject(metadata?.get("maintainer"))

			val contact = ApUtilityService.extractString(maintainer?.get("email"))

			val software = ApUtilityService.extractObject(nodeinfo["software"])
			val softwareName = ApUtilityService.extractString(software?.get("name"))
			val version = ApUtilityService.extractString(software?.get("version"))

			return PartialInstance(
				id = id,
				host = host,

				name = name,
				description = description,
				color = color,
				icon = null,

				software = softwareName,
				version = version,
				contact = contact,

				createdAt = null,
				updatedAt = if (existing != null) TimeService.now() else null,
			)
		}

		fun update(instance: PartialInstance): InstanceEntity? {
			try {
				transaction {
					InstanceEntity.findByIdAndUpdate(instance.id!!) {
						it.host = instance.host!!

						it.name = instance.name
						it.description = instance.description
						if (instance.color != null)
							it.color = instance.color!!
						it.icon = instance.icon

						it.software = instance.software
						it.version = instance.version
						it.contact = instance.contact

						it.updatedAt = instance.updatedAt
					}
				}

				return getById(instance.id!!)
			} catch (_: Exception) {
				return null
			}
		}

		fun register(instance: PartialInstance): InstanceEntity? {
			try {
				transaction {
					InstanceEntity.new(instance.id) {
						this.host = instance.host!!

						this.name = instance.name
						this.description = instance.description
						if (instance.color != null)
							this.color = instance.color!!
						this.icon = instance.icon

						this.software = instance.software
						this.version = instance.version
						this.contact = instance.contact

						this.updatedAt = instance.updatedAt
					}
				}

				return getById(instance.id!!)
			} catch (_: Exception) {
				return null
			}
		}
	}
}