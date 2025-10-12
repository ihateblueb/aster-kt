package site.remlit.blueb.aster

import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import site.remlit.blueb.aster.route.admin.adminIndex
import site.remlit.blueb.aster.route.ap.apNote
import site.remlit.blueb.aster.route.ap.apUser
import site.remlit.blueb.aster.route.ap.hostMeta
import site.remlit.blueb.aster.route.ap.inbox
import site.remlit.blueb.aster.route.ap.nodeInfo
import site.remlit.blueb.aster.route.ap.outbox
import site.remlit.blueb.aster.route.ap.webfinger
import site.remlit.blueb.aster.route.api.login
import site.remlit.blueb.aster.route.api.meta
import site.remlit.blueb.aster.route.api.mod.modInvite
import site.remlit.blueb.aster.route.api.mod.modPolicy
import site.remlit.blueb.aster.route.api.note
import site.remlit.blueb.aster.route.api.register
import site.remlit.blueb.aster.route.api.timeline
import site.remlit.blueb.aster.route.api.user
import site.remlit.blueb.aster.route.frontend

fun Application.configureRouting() {
	routing {
		swaggerUI(path = "swagger", swaggerFile = "openapi.yaml")

		adminIndex()

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
