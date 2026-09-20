package com.example

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.ApplicationConfig

fun createDataSource(config: ApplicationConfig): HikariDataSource = HikariDataSource(HikariConfig().apply {
    jdbcUrl = "jdbc:postgresql://${config.stringOrDefault("database.host", "localhost")}:" +
        "${config.stringOrDefault("database.port", "5432")}/" +
        config.stringOrDefault("database.name", "maintena")
    username = config.stringOrDefault("database.user", "maintena")
    password = requireNotNull(config.propertyOrNull("database.password")?.getString()) {
        "database.password must be supplied through DB_PASSWORD"
    }
    driverClassName = "org.postgresql.Driver"
    maximumPoolSize = 5
})

private fun ApplicationConfig.stringOrDefault(path: String, default: String): String =
    propertyOrNull(path)?.getString()?.ifBlank { default } ?: default
