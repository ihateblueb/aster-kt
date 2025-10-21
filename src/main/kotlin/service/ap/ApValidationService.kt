package site.remlit.blueb.aster.service.ap

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import site.remlit.blueb.aster.common.model.type.PolicyType
import site.remlit.blueb.aster.db.entity.UserEntity
import site.remlit.blueb.aster.model.Configuration
import site.remlit.blueb.aster.model.Service
import site.remlit.blueb.aster.model.ap.ApValidationException
import site.remlit.blueb.aster.model.ap.ApValidationExceptionType
import site.remlit.blueb.aster.service.IdentifierService
import site.remlit.blueb.aster.service.KeypairService
import site.remlit.blueb.aster.service.PolicyService
import site.remlit.blueb.httpSignatures.HttpSignature
import java.security.MessageDigest
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.encoding.Base64

/**
 * Service for validating ActivityPub activities.
 *
 * @since 2025.5.1.0-SNAPSHOT
 * */
class ApValidationService : Service() {
	companion object {
		private val logger: Logger = LoggerFactory.getLogger(ApValidationService::class.java)

		suspend fun validate(request: RoutingRequest, body: ByteArray): UserEntity? {
			val validationRequestId = IdentifierService.generate()

			val blockPolicies = PolicyService.getAllByType(PolicyType.Block)
			PolicyService.reducePoliciesInListToHost(blockPolicies)

			if (
				request.headers["Host"].isNullOrEmpty() ||
				request.headers["Host"] != Configuration.url.host
			) {
				logger.debug("[{}] Missing or invalid host.", validationRequestId)
				throw ApValidationException(
					ApValidationExceptionType.Unauthorized,
					"Missing or invalid host."
				)
			}

			if (
				request.headers["Date"].isNullOrEmpty()
			) {
				logger.debug("[{}] Date not present.", validationRequestId)
				throw ApValidationException(
					ApValidationExceptionType.Unauthorized,
					"Date not present."
				)
			}

			if (
				request.headers["Digest"].isNullOrEmpty()
			) {
				logger.debug("[{}] Digest not present.", validationRequestId)
				throw ApValidationException(
					ApValidationExceptionType.Unauthorized,
					"Digest not present."
				)
			}

			if (
				request.headers["Signature"].isNullOrEmpty()
			) {
				logger.debug("[{}] Signature not present.", validationRequestId)
				throw ApValidationException(
					ApValidationExceptionType.Unauthorized,
					"Signature not present."
				)
			}

			if (
				!request.headers["Digest"]!!.startsWith("SHA-256=")
			) {
				logger.debug("[{}] Digest uses unsupported algorithm.", validationRequestId)
				throw ApValidationException(
					ApValidationExceptionType.Unauthorized,
					"Digest uses unsupported algorithm."
				)
			}

			if (
				isDigestValid(request.headers["Digest"]!!, body)
			) {
				logger.debug("[{}] Digest invalid.", validationRequestId)
				throw ApValidationException(
					ApValidationExceptionType.Unauthorized,
					"Digest invalid."
				)
			}

			val httpSignature = HttpSignature.parseHeaderString(request.headers["Signature"]!!)
			val actorApId = httpSignature.keyId.substringBefore("#")

			val actor = ApActorService.resolve(actorApId)
			if (actor == null) {
				logger.debug("[{}] Actor not found.", validationRequestId)
				throw ApValidationException(
					ApValidationExceptionType.Unauthorized,
					"Actor not found."
				)
			}

			val signatureHeaderValues = mutableListOf<String>()
			for (header in httpSignature.headers.filter { it != "(request-target)" }) {
				signatureHeaderValues.add(
					request.headers[header] ?: throw ApValidationException(
						ApValidationExceptionType.Unauthorized,
						"Headers specified in signature not included in request"
					)
				)
			}

			val signingString = httpSignature.createSigningString(
				when (request.httpMethod) {
					HttpMethod.Get -> site.remlit.blueb.httpSignatures.HttpMethod.GET
					HttpMethod.Post -> site.remlit.blueb.httpSignatures.HttpMethod.POST
					else -> throw ApValidationException(
						ApValidationExceptionType.Unauthorized,
						"Unsupported HTTP method"
					)
				},
				request.path(),
				signatureHeaderValues
			)

			val isSignatureValid = try {
				httpSignature.verify(
					signingString,
					KeypairService.pemToPublicKey(actor.publicKey),
					parseHttpDate(request.headers["Date"]!!)
				)
			} catch (e: Exception) {
				logger.debug("[{}] Signature invalid. {}", validationRequestId, e.message)
				throw ApValidationException(
					ApValidationExceptionType.Forbidden,
					"Signature invalid: ${e.message}"
				)
			}

			if (
				!isSignatureValid
			) {
				logger.debug("[{}] Signature invalid", validationRequestId)
				throw ApValidationException(
					ApValidationExceptionType.Forbidden,
					"Signature invalid."
				)
			}

			return actor
		}

		fun isDigestValid(digest: String, data: ByteArray): Boolean {
			val md = MessageDigest.getInstance("SHA-256")
			val ourDigest = md.digest(data)

			return digest == Base64.encode(ourDigest)
		}

		fun parseHttpDate(date: String): LocalDateTime {
			return ZonedDateTime
				.parse(date, DateTimeFormatter.RFC_1123_DATE_TIME)
				.withZoneSameInstant(ZoneId.systemDefault())
				.toLocalDateTime()
				.toKotlinLocalDateTime()
		}
	}
}


