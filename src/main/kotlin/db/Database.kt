package me.blueb.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import me.blueb.model.Configuration
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig

val configuration = Configuration()

object Database {
	val config = HikariConfig().apply {
		jdbcUrl = "jdbc:postgresql://${configuration.database.host}:${configuration.database.port}/${configuration.database.db}"
		username = configuration.database.user
		password = configuration.database.password
	}

	val dataSource = HikariDataSource(config)

	val database by lazy {
		Database.connect(datasource = dataSource)
	}
}
