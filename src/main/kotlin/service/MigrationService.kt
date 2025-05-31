package me.blueb.service

import MigrationUtils
import me.blueb.db.Database
import me.blueb.db.table.*
import me.blueb.model.Configuration
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.ExperimentalDatabaseMigrationApi
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@OptIn(ExperimentalDatabaseMigrationApi::class)
class MigrationService {
	private val logger: Logger = LoggerFactory.getLogger(this::class.java)

	private val configuration = Configuration()
	private val identifierService = IdentifierService()

	private val migrationPath: String = "src/main/resources/migrations"
	private val database = Database.database

	val flyway: Flyway = Flyway.configure()
		.dataSource("jdbc:postgresql://${configuration.database.host}:${configuration.database.port}/${configuration.database.db}", configuration.database.user, configuration.database.password)
		.locations("filesystem:$migrationPath")
		.load()

	fun generate() {
		transaction(database) {
			// todo: automatically look for these
			val tables = listOf(
				AuthTable,
				InviteTable,
				NoteTable,
				NotificationTable,
				PolicyTable,
				RelationshipTable,
				RoleTable,
				UserTable,
				UserPrivateTable,
			)

			for (table in tables) {
				logger.info("Generating migration script for table " + table.tableName)
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
