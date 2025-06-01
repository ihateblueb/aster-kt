package site.remlit.blueb

import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.forwardedheaders.*
import io.ktor.server.request.*
import kotlinx.coroutines.runBlocking
import site.remlit.blueb.db.Database
import site.remlit.blueb.model.Configuration
import site.remlit.blueb.service.CommandLineService
import site.remlit.blueb.service.IdentifierService
import site.remlit.blueb.service.PluginService
import site.remlit.blueb.service.SetupService
import site.remlit.blueb.util.jsonConfig

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
