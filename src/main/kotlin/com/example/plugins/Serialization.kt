package com.example.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Application.configureSerialization() {
    // ContentNegotiation is responsible to negotiate content between client and the server,
    // examine the 'Accept' header and see if it can serve this specific type of content
    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/json/ktx-serialization") {
                call.respond(
                        mapOf(
                                "hello" to "world",
                                "key" to "value"
                        )
                )
        }
    }
}
