package site.remlit.aster.service.ap

import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinLocalDateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import site.remlit.aster.common.model.type.PolicyType
import site.remlit.aster.db.entity.UserEntity
import site.remlit.aster.model.Configuration
import site.remlit.aster.model.Service
import site.remlit.aster.model.ap.ApValidationException
import site.remlit.aster.model.ap.ApValidationExceptionType
import site.remlit.aster.service.IdentifierService
import site.remlit.aster.service.KeypairService
import site.remlit.aster.service.PolicyService
import java.security.MessageDigest
import java.security.PublicKey
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.encoding.Base64
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

/**
 * Service for validating ActivityPub activities.
 *
 * @since 2025.5.1.0-SNAPSHOT
 * */
class ApValidationService : Service() {
	companion object {
		private val logger: Logger = LoggerFactory.getLogger(ApValidationService::class.java)

		suspend fun validate(request: RoutingRequest, body: ByteArray): UserEntity {
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

			val signatureHeader = request.headers["Signature"]!!
			println(signatureHeader)

			val keyIdRegex = buildHeaderRegex("keyId")
			val algorithmRegex = buildHeaderRegex("algorithm")
			val headersRegex = buildHeaderRegex("headers")
			val signatureRegex = buildHeaderRegex("signature")

			val sigKeyId = keyIdRegex.find(signatureHeader)?.groups?.get(1)?.value
				?: throw ApValidationException(
					ApValidationExceptionType.Unauthorized,
					"Could not extract keyId from Signature header"
				)
			val sigAlgorithm = algorithmRegex.find(signatureHeader)?.groups?.get(1)?.value
				?: throw ApValidationException(
					ApValidationExceptionType.Unauthorized,
					"Could not extract algorithm from Signature header"
				)
			val sigHeaders = headersRegex.find(signatureHeader)?.groups?.get(1)?.value?.split(" ")
				?: throw ApValidationException(
					ApValidationExceptionType.Unauthorized,
					"Could not extract headers from Signature header"
				)
			val sigSignature = signatureRegex.find(signatureHeader)?.groups?.get(1)?.value
				?: throw ApValidationException(
					ApValidationExceptionType.Unauthorized,
					"Could not extract signature from Signature header"
				)

			val signingString = ApSignatureService.createSigningString(
				request.httpMethod,
				request.path(),
				sigHeaders,
				ApSignatureService.headersToMap(request.headers)
			)

			println("Sig = $signatureHeader")

			val actorApId = sigKeyId.substringBefore("#")

			val actor = ApActorService.resolve(actorApId)
			if (actor == null) {
				logger.debug("[{}] Actor not found.", validationRequestId)
				throw ApValidationException(
					ApValidationExceptionType.Unauthorized,
					"Actor not found."
				)
			}

			val isSignatureValid = try {
				isSignatureValid(
					sigSignature,
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

		@OptIn(ExperimentalTime::class)
		fun isSignatureValid(
			signature: String,
			signingString: String,
			publicKey: PublicKey,
			date: LocalDateTime
		): Boolean {
			// 150s is 2.5m, total of 5m window.
			// Iceshrimp.NET also does 5m.
			val maxTimeMargin = 150

			val nowPlusMargin = Clock.System.now().plus(maxTimeMargin.seconds)
			val nowMinusMargin = Clock.System.now().minus(maxTimeMargin.seconds)

			val dateInstant = date.toInstant(TimeZone.currentSystemDefault())

			if (dateInstant > nowPlusMargin)
				throw ApValidationException(
					ApValidationExceptionType.Forbidden,
					"Date is more than $maxTimeMargin seconds past now."
				)

			if (dateInstant < nowMinusMargin)
				throw ApValidationException(
					ApValidationExceptionType.Forbidden,
					"Date is more than $maxTimeMargin seconds from now."
				)

			println(signingString)

			val javaSignature = java.security.Signature.getInstance("SHA256withRSA")
			javaSignature.initVerify(publicKey)
			javaSignature.update(signingString.toByteArray())

			return javaSignature.verify(Base64.decode(signature))
		}

		fun parseHttpDate(date: String): LocalDateTime {
			return ZonedDateTime
				.parse(date, DateTimeFormatter.RFC_1123_DATE_TIME)
				.withZoneSameInstant(ZoneId.systemDefault())
				.toLocalDateTime()
				.toKotlinLocalDateTime()
		}

		/**
		 * Creates a regex pattern to get the value of a `key="value"` pattern.
		 * */
		fun buildHeaderRegex(key: String) = Regex("$key=\"(.*?)\"")
	}
}


