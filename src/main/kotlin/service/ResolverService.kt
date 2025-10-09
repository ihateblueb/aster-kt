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
import org.jetbrains.annotations.ApiStatus
import org.slf4j.LoggerFactory
import site.remlit.blueb.aster.exception.ResolverException
import site.remlit.blueb.aster.model.Configuration
import site.remlit.blueb.aster.model.PackageInformation
import site.remlit.blueb.aster.model.PolicyType
import site.remlit.blueb.aster.model.Service
import site.remlit.blueb.aster.util.jsonConfig

class ResolverService : Service() {
	companion object {
		private val logger = LoggerFactory.getLogger(this::class.java)

		fun createClient(): HttpClient {
			return HttpClient(CIO) {
				defaultRequest {
					headers.append(
						"User-Agent",
						"${PackageInformation.name}/${PackageInformation.version} (+${Configuration.url})"
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
		 * @param url URL to resolve as a String
		 * @param accept Accept header's content, defaults to application/activity+json
		 *
		 * @return [JsonObject] or null
		 * */
		suspend fun resolve(url: String, accept: String = "application/activity+json"): JsonObject? {
			val id = IdentifierService.generate()

			val blockPolicies = PolicyService.getAllByType(PolicyType.Block)
			val blockedHosts = PolicyService.reducePoliciesInListToHost(blockPolicies)

			if (blockedHosts.contains(Url(url).host))
				return null

			try {
				val client = createClient()
				val response = client.get(url) {
					headers.append("Accept", accept)
				}
				client.close()

				logger.info("${response.status} ${response.request.method} - ${response.request.url}")

				if (response.status != HttpStatusCode.OK)
					throw ResolverException(response.status, response.status.description)

				val body: JsonObject? = response.body()
				return body
			} catch (e: Exception) {
				return null
			}
		}

		/**
		 * Resolve a URL, signed
		 *
		 * @param url URL to resolve as a String
		 * @param accept Accept header's content, defaults to application/activity+json
		 *
		 * @return [JsonObject] or null
		 * */
		@ApiStatus.Experimental
		suspend fun resolveSigned(url: String, accept: String = "application/activity+json"): JsonObject? {
			return null
		}
	}
}
