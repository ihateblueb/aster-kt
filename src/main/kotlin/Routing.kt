package me.blueb

import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.*

import me.blueb.routes.ap.*
import me.blueb.routes.api.*

fun Application.configureRouting() {
    routing {
        swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")

        nodeinfo()
        apUser()

        register()
        user()
    }
}
