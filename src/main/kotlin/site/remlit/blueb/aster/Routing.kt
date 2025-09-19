package site.remlit.blueb.aster

import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import site.remlit.blueb.aster.route.ap.*
import site.remlit.blueb.aster.route.api.*
import site.remlit.blueb.aster.route.api.mod.modInvite
import site.remlit.blueb.aster.route.api.mod.modPolicy
import site.remlit.blueb.aster.route.frontend

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
		modInvite()

		login()
		meta()
		note()
		register()
		timeline()
		user()

		frontend()
	}
}
