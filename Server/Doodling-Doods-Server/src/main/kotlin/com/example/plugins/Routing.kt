package com.example.plugins

import com.example.playerManager.PlayerCommunicationManager
import com.example.playerManager.socket
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(communicationManager: PlayerCommunicationManager) {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
        }
    }
    install(DoubleReceive)
    routing {
        socket(communicationManager)
    }
    routing {
        get("/") {
            call.respondText("Hello welcome to Doodling Doods!!")
        }
        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
        post("/double-receive") {
            val first = call.receiveText()
            val theSame = call.receiveText()
            call.respondText(first + " " + theSame)
        }
    }
}
