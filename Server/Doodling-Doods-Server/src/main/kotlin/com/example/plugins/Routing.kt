package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*

<<<<<<< Updated upstream
fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
        }
    }
    install(DoubleReceive)
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
        post("/double-receive") {
            val first = call.receiveText()
            val theSame = call.receiveText()
            call.respondText(first + " " + theSame)
=======
fun Application.configureRouting(communicationManager: PlayerCommunicationManager) {
//    install(StatusPages) {
//        exception<Throwable> { call, cause ->
//            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
//        }
//    }
//    install(DoubleReceive)
    routing {
        socket(communicationManager)
    }
    routing {
        webSocket("/chat") {
            send("You are connected!")
            for(frame in incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                send("You said: $receivedText")
            }
>>>>>>> Stashed changes
        }
    }
//    routing {
//        get("/why") {
//            call.respondText("Hello edhuku!!")
//        }
//    }
//    routing {
//        get("/") {
//            call.respondText("Hello welcome to Doodling Doods!!")
//        }
//        // Static plugin. Try to access `/static/index.html`
//        static("/static") {
//            resources("static")
//        }
//        post("/double-receive") {
//            val first = call.receiveText()
//            val theSame = call.receiveText()
//            call.respondText(first + " " + theSame)
//        }
//    }
}
