package me.blueb.service

import MigrationUtils
import me.blueb.db.Database
import me.blueb.db.table.*
import me.blueb.model.Configuration
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.ExperimentalDatabaseMigrationApi
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

const val migrationPath: String = "src/main/resources/migrations"

val configuration = Configuration()
val identifierService = IdentifierService()
val log = LoggerFactory.getLogger("MigrationService")

val database = Database.database

@OptIn(ExperimentalDatabaseMigrationApi::class)
class MigrationService {
	val flyway = Flyway.configure()
		.dataSource("jdbc:postgresql://${configuration.database.host}:${configuration.database.port}/${configuration.database.db}", configuration.database.user, configuration.database.password)
		.locations("filesystem:$migrationPath")
		.load()

	fun generate() {
		transaction() {
			// todo: automatically look for these
			val tables = listOf(
				AuthTable,
				InviteTable,
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
