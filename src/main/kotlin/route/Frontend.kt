package site.remlit.blueb.route

import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Route.frontend() {
	singlePageApplication {
		val resource = javaClass.classLoader.getResource("frontend/dist")

		if (resource == null)
			throw RuntimeException("Frontend not properly packaged.")

		react(resource.toURI().path)
	}
}
