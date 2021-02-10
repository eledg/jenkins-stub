package com.elliotledger

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.auth.*
import io.ktor.http.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Authentication) {
        basic("myBasicAuth") {
            realm = "Jenkins stub"
            validate { if (it.name == "username" && it.password == "password") UserIdPrincipal(it.name) else null }
        }
    }

    routing {
        authenticate("myBasicAuth") {
            route("/job") {
                route("/job-name-with-params/buildWithParameters") {
                    post {
                        call.respond(HttpStatusCode.OK)
                    }
                }

                route("/job-name-with-params/build") {
                    post {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }


                route("/job-name-no-params/build") {
                    post {
                        call.respond(HttpStatusCode.OK)
                    }
                }

                route("/job-name-no-params/buildWithParameters") {
                    post {
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                }
            }
        }
    }
}


