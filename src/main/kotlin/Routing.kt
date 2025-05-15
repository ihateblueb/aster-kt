package me.blueb

import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.*
import me.blueb.route.ap.apUser
import me.blueb.route.api.register
import me.blueb.route.ap.*
import me.blueb.route.api.*

fun Application.configureRouting() {
    routing {
        swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")

		hostMeta()
		inbox()
        nodeinfo()
		apNote()
        apUser()
		webfinger()

		login()
		note()
        register()
        user()
    }
}
