package me.blueb.service.ap

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.callid.callId
import io.ktor.server.request.httpMethod
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import me.blueb.model.ApiError
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ApValidationService {
	private val logger: Logger = LoggerFactory.getLogger(this::class.java)

	suspend fun validate(call: RoutingCall) {
		val isGet = call.request.httpMethod == HttpMethod.Get
		val headers = if (isGet) call.request.headers["Accept"] else call.request.headers["Content-Type"]
		val digest = call.request.headers["Digest"]

		if (
			headers?.contains("application/activity+json")?.not() ?: false &&
			headers?.contains("application/ld+json")?.not() ?: false
		) {
			logger.debug("Validation failed, improper ${if (isGet) "Accept" else "Content-Type"} headers.")
			call.respond(
				HttpStatusCode.Unauthorized,
				ApiError(
					message = "Improper ${if (isGet) "Accept" else "Content-Type"} headers",
					requestId = call.callId
				)
			)
			return
		}

		if (digest.isNullOrEmpty()) {
			logger.debug("Validation failed, missing digest.")
			call.respond(
				HttpStatusCode.Unauthorized,
				ApiError(
					message = "Missing digest",
					requestId = call.callId
				)
			)
			return
		}

	}
}
