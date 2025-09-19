package site.remlit.blueb.aster.route

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Route.frontend() {
	singlePageApplication {
		val resource = this::class.java.classLoader.getResource("frontend")

		if (resource == null)
			application.log.warn("Frontend resource not found, disabling.")
		else
			react(resource.toURI().path)
	}
}
