package site.remlit.blueb.route

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Route.frontend() {
	singlePageApplication {
		val resource = javaClass.classLoader.getResource("frontend/dist")

		if (resource == null)
			application.log.warn("Frontend resource not found, disabling.")
		else
			react(resource.toURI().path)
	}
}
