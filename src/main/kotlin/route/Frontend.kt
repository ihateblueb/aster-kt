package me.blueb.route

import io.ktor.server.http.content.react
import io.ktor.server.http.content.singlePageApplication
import io.ktor.server.routing.Route

fun Route.frontend() {
	singlePageApplication {
		react(javaClass.classLoader.getResource("frontend/dist")!!.toURI().path)
	}
}
