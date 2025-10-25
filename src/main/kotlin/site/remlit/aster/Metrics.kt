package site.remlit.aster

import io.ktor.server.application.*
import io.ktor.server.metrics.micrometer.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.binder.system.UptimeMetrics
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import site.remlit.aster.route.RouteRegistry

fun Application.configureMetrics() {
	val appMicrometerRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
	install(MicrometerMetrics) {
		registry = appMicrometerRegistry

		meterBinders = listOf(
			JvmThreadMetrics(),
			JvmMemoryMetrics(),
			JvmGcMetrics(),
			ClassLoaderMetrics(),
			ProcessorMetrics(),
			UptimeMetrics(),
		)

		timers { call, exception ->
			tag("useragent", call.request.headers["User-Agent"] ?: "Unknown")
		}
	}

	RouteRegistry.registerRoute {
		get("/metrics") {
			call.respond(appMicrometerRegistry.scrape())
		}
	}
}