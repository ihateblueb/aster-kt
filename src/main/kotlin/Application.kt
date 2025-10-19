package site.remlit.blueb.aster

import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.forwardedheaders.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import site.remlit.blueb.aster.common.model.ApiError
import site.remlit.blueb.aster.db.Database
import site.remlit.blueb.aster.event.EventRegistry
import site.remlit.blueb.aster.event.application.ApplicationBeginShutdownEvent
import site.remlit.blueb.aster.event.application.ApplicationBeginStartEvent
import site.remlit.blueb.aster.event.application.ApplicationFinishShutdownEvent
import site.remlit.blueb.aster.event.application.ApplicationFinishStartEvent
import site.remlit.blueb.aster.model.ApiException
import site.remlit.blueb.aster.model.Configuration
import site.remlit.blueb.aster.model.ap.ApValidationException
import site.remlit.blueb.aster.model.ap.ApValidationExceptionType
import site.remlit.blueb.aster.plugin.PluginRegistry
import site.remlit.blueb.aster.service.CommandLineService
import site.remlit.blueb.aster.service.IdentifierService
import site.remlit.blueb.aster.service.MigrationService
import site.remlit.blueb.aster.service.PluginService
import site.remlit.blueb.aster.service.SetupService
import site.remlit.blueb.aster.util.jsonConfig
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {
	if (args.isNotEmpty() && !args[0].startsWith("-")) {
		runBlocking {
			CommandLineService.execute(args)
		}
		return
	}

	val server = embeddedServer(Netty, Configuration.port, Configuration.host, module = Application::module)

	ApplicationBeginStartEvent().call()

	Runtime.getRuntime().addShutdownHook(Thread {
		Thread.currentThread().name = "ShutdownMain"
		runBlocking {
			server.stop(1, 10, TimeUnit.SECONDS)
			Database.dataSource.close()
		}
		ApplicationFinishShutdownEvent().call()
	})

	server.start(wait = true)
}

fun Application.module() {
	// this hook completes first, always
	Runtime.getRuntime().addShutdownHook(Thread {
		Thread.currentThread().name = "ShutdownApplication"
		this.log.info("Shutting down...")
		ApplicationBeginShutdownEvent().call()
		PluginRegistry.disableAll()
		EventRegistry.clearListeners()
	})

	PluginService.initialize()

	// access connection before using it
	Database.connection

	MigrationService.isUpToDate()
	configureQueue()

	runBlocking {
		SetupService.setup()
	}

	install(CallLogging) {
		filter { call ->
			!call.request.uri.startsWith("/assets") &&
					!call.request.uri.startsWith("/favicon") &&
					!call.request.uri.startsWith("/installHook.js") &&
					!call.request.uri.startsWith("/manifest.json")
		}
		format { call ->
			val method = call.request.httpMethod.value
			val status = call.response.status()?.value
			val uri = call.request.uri

			"$status $method    $uri"
		}
	}

	install(CallId) {
		header(HttpHeaders.XRequestId)
		generate { IdentifierService.generate() }
	}

	install(CORS) {
		anyMethod()

		allowHost(Configuration.url.host)
		allowHost("127.0.0.1:9978")
		allowHost("127.0.0.1:5173")
	}

	install(AutoHeadResponse)
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

	install(StatusPages) {
		exception<Throwable> { call, cause ->
			if (cause is ApiException) {
				call.respond(
					cause.status,
					ApiError(
						cause.message,
						call.callId,
						cause.stackTrace.joinToString("\n")
					)
				)
			}

			if (cause is ApValidationException) {
				call.respond(
					if (cause.type == ApValidationExceptionType.Unauthorized) HttpStatusCode.Unauthorized else HttpStatusCode.Forbidden,
					ApiError(
						cause.message,
						call.callId,
						cause.stackTrace.joinToString("\n")
					)
				)
				return@exception
			}

			call.respond(
				HttpStatusCode.InternalServerError, ApiError(
					cause.message,
					call.callId,
					cause.stackTrace.joinToString("\n")
				)
			)
			return@exception
		}
	}

	configureAuthentication()
	configureRouting()

	ApplicationFinishStartEvent().call()
}
