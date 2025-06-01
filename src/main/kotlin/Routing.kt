package site.remlit.blueb

import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import site.remlit.blueb.route.ap.*
import site.remlit.blueb.route.api.login
import site.remlit.blueb.route.api.mod.modPolicy
import site.remlit.blueb.route.api.note
import site.remlit.blueb.route.api.register
import site.remlit.blueb.route.api.user
import site.remlit.blueb.route.frontend

fun Application.configureRouting() {
	routing {
		swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")

		hostMeta()
		nodeInfo()
		webfinger()

		inbox()
		outbox()

		apNote()
		apUser()

		modPolicy()

		login()
		note()
		register()
		user()

		frontend()
	}
}
