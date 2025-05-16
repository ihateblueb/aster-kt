package me.blueb

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import me.blueb.db.Database
import me.blueb.model.Configuration
import me.blueb.service.IdentifierService
import me.blueb.service.MigrationService
import me.blueb.service.PluginService
import me.blueb.service.SetupService

private val identifierService = IdentifierService()
private val setupService = SetupService()
private val pluginService = PluginService()

private val configuration = Configuration()

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
	// access connection before using it
	Database.database

	configureQueue()

	runBlocking {
		setupService.setup()
	}

	pluginService.initialize()

	install(CallId) {
		header(HttpHeaders.XRequestId)
		generate { identifierService.generate() }
	}

	install(ContentNegotiation) {
		json(
			Json {
				encodeDefaults = true
				prettyPrint = true
				isLenient = true
			},
		)
	}

	configureAuthentication()
	configureRouting()
}
