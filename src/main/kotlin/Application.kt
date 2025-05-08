package me.blueb

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.resources.*
import kotlinx.serialization.json.Json

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain
        .main(args)
}

fun Application.module() {
    install(Resources)
    install(RequestValidation) {}
    install (ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
            },
        )
    }

    /*
        refactor "exposed/Exposed*" to "entity/*Entity"
        rename directories to be singular

        maven -> gradle?

        scan plugins folder for jars, 
        load,
        @*Plugin annotated classes,
        and other stuff

        look how DI works with these plugins, write sample

        also consider annotation for route to automatically add to Routing.kt,
        use for route plugins
    */

    configureDatabases()
    configureRouting()
}
