package main

import com.elliotledger.module
import io.ktor.http.*
import io.ktor.http.auth.*
import io.ktor.server.testing.*
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals


class JenkinsStubTest {
    private val username = "username"
    private val password = "password"


    @Test
    fun getResponseBuildWithNoParams() {
        withTestApplication({ module(testing = true) }) {

            handleRequest(HttpMethod.Post, "/job/job-name-no-params/build") {
                addHeader(
                    HttpHeaders.Authorization,
                    HttpAuthHeader.Single(
                        "basic",
                        Base64.getEncoder().encodeToString("$username:$password".toByteArray())
                    ).render()
                )
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun getResponseBuildWithParams() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "/job/job-name-with-params/buildWithParameters?param=GOOD_PARAM") {
                addHeader(
                    HttpHeaders.Authorization,
                    HttpAuthHeader.Single(
                        "basic",
                        Base64.getEncoder().encodeToString("$username:$password".toByteArray())
                    ).render()
                )
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun testBuildUnexpectedParams() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "/job/job-name-no-params/buildWithParameters?param=wrong") {
                addHeader(
                    HttpHeaders.Authorization,
                    HttpAuthHeader.Single(
                        "basic",
                        Base64.getEncoder().encodeToString("$username:$password".toByteArray())
                    ).render()
                )
            }.apply {
                assertEquals(HttpStatusCode(500, "Internal Server Error"), response.status())
            }
        }
    }

    @Test
    fun testBuildMissingParamsAndWrongBuildMethod() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "/job/job-name-with-params/build") {
                addHeader(
                    HttpHeaders.Authorization,
                    HttpAuthHeader.Single(
                        "basic",
                        Base64.getEncoder().encodeToString("$username:$password".toByteArray())
                    ).render()
                )
            }.apply {
                assertEquals(HttpStatusCode(400, "Bad Request Error"), response.status())
            }
        }
    }
    @Test
    fun testBuildMissingParams() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "/job/job-name-with-params/buildWithParameters") {
                addHeader(
                    HttpHeaders.Authorization,
                    HttpAuthHeader.Single(
                        "basic",
                        Base64.getEncoder().encodeToString("$username:$password".toByteArray())
                    ).render()
                )
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun testBuildMissspeltParams() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "/job/job-name-with-params/buildWithParameters?param=wrong") {
                addHeader(
                    HttpHeaders.Authorization,
                    HttpAuthHeader.Single(
                        "basic",
                        Base64.getEncoder().encodeToString("$username:$password".toByteArray())
                    ).render()
                )
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }


    @Test
    fun testJobNotFound() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "/job/job-that-doesn't-exist/build") {
                addHeader(
                    HttpHeaders.Authorization,
                    HttpAuthHeader.Single(
                        "basic",
                        Base64.getEncoder().encodeToString("$username:$password".toByteArray())
                    ).render()
                )
            }.apply {
                assertEquals(HttpStatusCode(404, "Not Found"), response.status())
            }
        }
    }

    @Test
    fun testAuthenticationFailed() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "/job/job-name-no-params/build").apply {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }
}