package me.blueb

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.resources.*
import kotlinx.serialization.json.Json
import me.blueb.service.IdentifierService


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain
        .main(args)
}

fun Application.module() {
    val identifierService = IdentifierService()

    install(CallId) {
        header(HttpHeaders.XRequestId)
        verify { callId: String ->
            callId.isNotEmpty()
        }
        generate { identifierService.generate() }
    }

    install(Resources)

    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
            },
        )
    }

    configureDatabases()
    configureRouting()
}
