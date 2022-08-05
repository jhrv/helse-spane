package no.nav.helse.spane

import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.helse.logger
import no.nav.helse.spane.db.PersonRepository

fun ktorServer(database: PersonRepository): ApplicationEngine =
    embeddedServer(CIO, applicationEngineEnvironment {

        log = logger
        connector {
            port = 8080
        }
        module {
            install(ContentNegotiation) { jackson() }
            install(CallLogging) {
                disableDefaultColors()
                filter { call ->
                    call.request.path().startsWith("/hello")
                }
            }

            routing {
                get("/") {
                    call.respondText(
                        this::class.java.classLoader.getResource("static/index.html")!!.readText(),
                        ContentType.Text.Html
                    )
                }
                static("/") {
                    resources("static/")
                }
                get("/isalive") {
                    call.respondText("OK")
                }

                get("/isready") {
                    call.respondText("OK")
                }


                get("/fnr/{id?}") {
                    val id = call.parameters["id"] ?: return@get call.respondText(
                        "Missing id",
                        status = HttpStatusCode.BadRequest
                    )
                    val person = database.hentPerson(id)?.deserialiser() ?: return@get call.respond(
                        HttpStatusCode.NotFound,
                        "not found"
                    )
                    val apiVisitor = APIVisitor()
                    person.accept(apiVisitor)
                    call.respondText(
                        contentType = ContentType.Application.Json,
                        text = objectMapper.writeValueAsString(apiVisitor.personMap)
                    )

                }
            }
        }
    })