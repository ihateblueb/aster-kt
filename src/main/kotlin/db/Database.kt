package site.remlit.blueb.aster.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.v1.jdbc.Database
import site.remlit.blueb.aster.model.Configuration

val configuration = Configuration()

object Database {
	val config = HikariConfig().apply {
		jdbcUrl =
			"jdbc:postgresql://${configuration.database.host}:${configuration.database.port}/${configuration.database.db}"
		username = configuration.database.user
		password = configuration.database.password
	}

	val dataSource = HikariDataSource(config)

	val connection = Database.connect(dataSource)
}
