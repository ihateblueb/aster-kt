package site.remlit.blueb.aster.service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.JsonObject
import org.slf4j.LoggerFactory
import site.remlit.blueb.aster.model.Configuration
import site.remlit.blueb.aster.model.PackageInformation
import site.remlit.blueb.aster.model.PolicyType
import site.remlit.blueb.aster.util.jsonConfig

class ResolverService {
	private val logger = LoggerFactory.getLogger(this::class.java)

	private val packageInformation = PackageInformation()
	private val configuration = Configuration()

	private val identifierService = IdentifierService()
	private val policyService = PolicyService()

	fun createClient(): HttpClient {
		return HttpClient(CIO) {
			defaultRequest {
				headers.append(
					"User-Agent",
					"${packageInformation.name}/${packageInformation.version} (+${configuration.url})"
				)
			}

			install(ContentNegotiation) {
				json(jsonConfig)

				register(
					ContentType.parse("application/ld+json"),
					KotlinxSerializationConverter(jsonConfig)
				)
				register(
					ContentType.parse("application/activity+json"),
					KotlinxSerializationConverter(jsonConfig)
				)
				register(
					ContentType.parse("application/jrd+json"),
					KotlinxSerializationConverter(jsonConfig)
				)
			}
		}
	}

	/**
	 * Resolve a URL
	 *
	 * @return [JsonObject] or null
	 * */
	suspend fun resolve(url: String, accept: String = "application/activity+json"): JsonObject? {
		val id = identifierService.generate()

		val blockPolicies = policyService.getAllByType(PolicyType.Block)
		val blockedHosts = policyService.reducePoliciesInListToHost(blockPolicies)

		if (blockedHosts.contains(Url(url).host))
			return null

		try {
			val client = createClient()
			val response = client.get(url) {
				headers.append("Accept", accept)
			}
			client.close()

			logger.info("${response.status} ${response.request.method} - ${response.request.url}")

			val body: JsonObject? = response.body()
			if (response.status == HttpStatusCode.OK)
				return if (body != null) return body else null
		} catch (e: Exception) {
			logger.error("Resolution $id failed! " + e.message)
			logger.debug("Resolution $id exception: ", e)
			return null
		}

		return null
	}

	/**
	 * Resolve a URL, signed
	 *
	 * @return [JsonObject] or null
	 * */
	suspend fun resolveSigned(url: String) {}
}
