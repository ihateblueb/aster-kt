package me.blueb

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.KotlinxSerializationConverter
import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.plugins.forwardedheaders.ForwardedHeaders
import io.ktor.server.request.httpMethod
import io.ktor.server.request.uri
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import me.blueb.db.Database
import me.blueb.model.Configuration
import me.blueb.service.CommandLineService
import me.blueb.service.IdentifierService
import me.blueb.service.PluginService
import me.blueb.service.SetupService

private val configuration = Configuration()

private val commandLineService = CommandLineService()
private val identifierService = IdentifierService()
private val setupService = SetupService()
private val pluginService = PluginService()

fun main(args: Array<String>) {
	if (args.isNotEmpty() && !args[0].startsWith("-")) {
		runBlocking {
			commandLineService.execute(args)
		}
		return
	}

	io.ktor.server.netty.EngineMain
		.main(args)
}

fun Application.module() {
	pluginService.initialize()

	// access connection before using it
	Database.database

	configureQueue()

	runBlocking {
		setupService.setup()
	}

	install(CallLogging) {
		format { call ->
			val method = call.request.httpMethod.value
			val status = call.response.status()
			val uri = call.request.uri

			println(call.request.headers["Accept"])

			"$status $method - $uri"
		}
	}

	install(CallId) {
		header(HttpHeaders.XRequestId)
		generate { identifierService.generate() }
	}

	install(CORS) {
		anyMethod()

		allowHost(configuration.url.host)
		allowHost("127.0.0.1:9978")
		allowHost("127.0.0.1:5173")
	}

	install(DefaultHeaders)
	install(ForwardedHeaders)

	install(ContentNegotiation) {
		val jsonConfig = Json {
			encodeDefaults = true
			prettyPrint = true
			isLenient = true
		}

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

	configureAuthentication()
	configureRouting()
}
