package me.blueb.service.ap

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.callid.callId
import io.ktor.server.request.httpMethod
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import me.blueb.model.ApiError
import me.blueb.model.Configuration
import me.blueb.model.PolicyType
import me.blueb.service.PolicyService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.security.MessageDigest
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class ApValidationService {
	private val logger: Logger = LoggerFactory.getLogger(this::class.java)
	private val configuration = Configuration()

	private val policyService = PolicyService()

	suspend fun validate(call: RoutingCall) {
		val isGet = call.request.httpMethod == HttpMethod.Get

		val body = call.receive<ByteArray>()

		val blockPolicies = policyService.getAllByType(PolicyType.Block)
		val blockedHosts: MutableList<String> = mutableListOf()

		for (blockPolicy in blockPolicies) {
			blockedHosts.add(blockPolicy.host)
		}

		if (
			call.request.headers["Host"].isNullOrEmpty() ||
			call.request.headers["Host"] != configuration.url.host
		) {
			logger.debug("Validation failed, missing or invalid host.")
			call.respond(
				HttpStatusCode.Unauthorized,
				ApiError(
					message = "Missing or invalid host",
					requestId = call.callId
				)
			)
			return
		}

		if (
			call.request.headers["Digest"].isNullOrEmpty()
		) {
			logger.debug("Validation failed, digest not present.")
			call.respond(
				HttpStatusCode.Unauthorized,
				ApiError(
					message = "Digest not present",
					requestId = call.callId
				)
			)
			return
		}

		if (
			!call.request.headers["Digest"]!!.startsWith("SHA-256=")
		) {
			logger.debug("Validation failed, digest incorrect algorithm.")
			call.respond(
				HttpStatusCode.Unauthorized,
				ApiError(
					message = "Digest incorrect algorithm",
					requestId = call.callId
				)
			)
			return
		}

		if (
			call.request.headers["Signature"].isNullOrEmpty()
		) {
			logger.debug("Validation failed, signature not present.")
			call.respond(
				HttpStatusCode.Unauthorized,
				ApiError(
					message = "Signature not present",
					requestId = call.callId
				)
			)
			return
		}

		if (
			isDigestValid(call.request.headers["Digest"]!!, body)
		) {
			logger.debug("Validation failed, digest invalid.")
			call.respond(
				HttpStatusCode.Unauthorized,
				ApiError(
					message = "Digest invalid",
					requestId = call.callId
				)
			)
			return
		}


	}

	@OptIn(ExperimentalEncodingApi::class)
	fun isDigestValid(digest: String, data: ByteArray): Boolean {
		val md = MessageDigest.getInstance("SHA-256")
		val ourDigest = md.digest(data)

		return digest == Base64.encode(ourDigest)
	}
}
