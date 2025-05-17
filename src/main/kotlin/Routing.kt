package me.blueb

import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.*
import me.blueb.route.ap.apUser
import me.blueb.route.api.register

import me.blueb.route.ap.*
import me.blueb.route.api.*
import me.blueb.route.api.admin.*

fun Application.configureRouting() {
    routing {
        swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")

		hostMeta()
        nodeinfo()
		webfinger()

		inbox()
		outbox()

		apNote()
        apUser()

		adminPolicy()

		login()
		note()
        register()
        user()
    }
}
