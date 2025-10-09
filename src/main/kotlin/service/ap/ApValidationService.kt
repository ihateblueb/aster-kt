package site.remlit.blueb.aster.service.ap

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import site.remlit.blueb.aster.model.Configuration
import site.remlit.blueb.aster.model.PolicyType
import site.remlit.blueb.aster.model.Service
import site.remlit.blueb.aster.model.ap.ApValidationException
import site.remlit.blueb.aster.model.ap.ApValidationExceptionType
import site.remlit.blueb.aster.service.KeypairService
import site.remlit.blueb.aster.service.PolicyService
import site.remlit.blueb.httpSignatures.HttpSignature
import site.remlit.blueb.httpSignatures.SignatureException
import java.security.MessageDigest
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.encoding.Base64

class ApValidationService : Service() {
	companion object {
		private val logger: Logger = LoggerFactory.getLogger(this::class.java)

		suspend fun validate(request: RoutingRequest, body: ByteArray) {
			request.httpMethod == HttpMethod.Get

			val blockPolicies = PolicyService.getAllByType(PolicyType.Block)
			PolicyService.reducePoliciesInListToHost(blockPolicies)

			if (
				request.headers["Host"].isNullOrEmpty() ||
				request.headers["Host"] != Configuration.url.host
			)
				throw ApValidationException(
					ApValidationExceptionType.Unauthorized,
					"Missing or invalid host."
				)

			if (
				request.headers["Date"].isNullOrEmpty()
			)
				throw ApValidationException(
					ApValidationExceptionType.Unauthorized,
					"Date not present."
				)

			if (
				request.headers["Digest"].isNullOrEmpty()
			)
				throw ApValidationException(
					ApValidationExceptionType.Unauthorized,
					"Digest not present."
				)

			if (
				request.headers["Signature"].isNullOrEmpty()
			)
				throw ApValidationException(
					ApValidationExceptionType.Unauthorized,
					"Signature not present."
				)

			if (
				!request.headers["Digest"]!!.startsWith("SHA-256=")
			)
				throw ApValidationException(
					ApValidationExceptionType.Unauthorized,
					"Digest uses unsupported algorithm."
				)

			if (
				isDigestValid(request.headers["Digest"]!!, body)
			)
				throw ApValidationException(
					ApValidationExceptionType.Unauthorized,
					"Digest invalid."
				)

			val parsedSignatureHeader = HttpSignature.parseHeaderString(request.headers["Signature"]!!)

			val actorApId = parsedSignatureHeader.keyId.substringBefore("#")

			val actor = ApActorService.resolve(actorApId) ?: throw ApValidationException(
				ApValidationExceptionType.Unauthorized,
				"Actor not found."
			)

			val isSignatureValid = try {
				parsedSignatureHeader.signature.verify(
					KeypairService.pemToPublicKey(actor.publicKey),
					parseHttpDate(request.headers["Date"]!!),
					body
				)
			} catch (e: SignatureException) {
				throw ApValidationException(
					ApValidationExceptionType.Forbidden,
					"Signature invalid: ${e.message}"
				)
			}

			if (
				!isSignatureValid
			)
				throw ApValidationException(
					ApValidationExceptionType.Forbidden,
					"Signature invalid."
				)
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


