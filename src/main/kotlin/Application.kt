package me.blueb

import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.serialization.json.Json
import me.blueb.tools.MigrationUtils
import kotlin.collections.contains

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, port = 8080) {
        module(args)
    }
    server.start(wait = true)
}

fun Application.module(args: Array<String>) {
    install(Resources)
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }

    if (args.contains("migrate")) {
        configureDatabases(true)
        engine.start(wait = true)
        return
    } else {
        configureDatabases()
    }

    configureRouting()
}