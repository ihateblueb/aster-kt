package me.blueb.service

import MigrationUtils
import me.blueb.db.database
import me.blueb.db.table.AuthTable
import me.blueb.db.table.NoteTable
import me.blueb.db.table.UserPrivateTable
import me.blueb.db.table.UserTable
import me.blueb.model.Configuration
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.ExperimentalDatabaseMigrationApi
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

const val migrationPath: String = "src/main/kotlin/migration"

val configuration = Configuration()
val identifierService = IdentifierService()
val log = LoggerFactory.getLogger("MigrationService")

@OptIn(ExperimentalDatabaseMigrationApi::class)
class MigrationService {
	val flyway = Flyway.configure()
		.dataSource("jdbc:postgresql://${configuration.database.host}:${configuration.database.port}/${configuration.database.db}", configuration.database.user, configuration.database.password)
		.locations("filesystem:$migrationPath")
		.load()

	fun generate() {
		transaction(database) {
			val tables = listOf(
				AuthTable,
				NoteTable,
				UserPrivateTable,
				UserTable,
			)

			for (table in tables) {
				log.info("Generating migration script for table " + table.tableName)
				MigrationUtils.generateMigrationScript(
					table,
					scriptDirectory = migrationPath,
					scriptName = "V${System.currentTimeMillis()}__Migration_${table.tableName}_${identifierService.generate()}",
				)
			}
		}
	}

	fun execute() {
		transaction(database) {
			flyway.migrate()
		}
	}
}
