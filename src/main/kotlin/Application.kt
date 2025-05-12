package me.blueb

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.resources.*
import kotlinx.serialization.json.Json
import me.blueb.service.IdentifierService
import me.blueb.service.MigrationService

fun main(args: Array<String>) {
	if (args.isNotEmpty()) {
		when (args[0]) {
			"migration:generate" -> {
				MigrationService().generate()
				return
			}
			"migration:execute" -> {
				MigrationService().execute()
				return
			}
			else -> println("Unknown command ${args[0]}.")
		}
	}

	io.ktor.server.netty.EngineMain
		.main(args)
}

fun Application.module() {
	val identifierService = IdentifierService()

	install(CallId) {
		header(HttpHeaders.XRequestId)
		verify { callId: String ->
			callId.isNotEmpty()
		}
		generate { identifierService.generate() }
	}

	install(Resources)

	install(ContentNegotiation) {
		json(
			Json {
				encodeDefaults = true
				prettyPrint = true
				isLenient = true
			},
		)
	}

	configureRouting()
}
