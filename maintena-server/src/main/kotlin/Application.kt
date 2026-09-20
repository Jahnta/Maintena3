package com.example

import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopping
import io.ktor.server.application.install
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.v1.jdbc.Database

fun Application.module() {
    configureHttp()

    val dataSource = createDataSource(environment.config)
    monitor.subscribe(ApplicationStopping) { dataSource.close() }

    Flyway.configure()
        .dataSource(dataSource)
        .load()
        .migrate()
    Database.connect(dataSource)

    configureRouting()
}

fun Application.configureHttp() {
    val applicationLog = environment.log

    install(CallLogging)
    install(ContentNegotiation) { json() }
    install(RequestValidation)
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            applicationLog.error("Unhandled request failure", cause)
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Internal server error"))
        }
    }
}
