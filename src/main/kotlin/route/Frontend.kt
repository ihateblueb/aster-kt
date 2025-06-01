package me.blueb.route

import io.ktor.server.http.content.react
import io.ktor.server.http.content.singlePageApplication
import io.ktor.server.routing.Route

fun Route.frontend() {
	singlePageApplication {
		val resource = javaClass.classLoader.getResource("frontend/dist")

		if (resource == null)
			throw RuntimeException("Frontend not properly packaged.")

		react(resource.toURI().path)
	}
}
