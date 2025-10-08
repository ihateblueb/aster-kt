package site.remlit.blueb.aster.service

import org.jetbrains.exposed.v1.core.ExperimentalDatabaseMigrationApi
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.migration.jdbc.MigrationUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import site.remlit.blueb.aster.db.Database
import site.remlit.blueb.aster.db.table.AuthTable
import site.remlit.blueb.aster.db.table.InviteTable
import site.remlit.blueb.aster.db.table.NoteLikeTable
import site.remlit.blueb.aster.db.table.NoteTable
import site.remlit.blueb.aster.db.table.NotificationTable
import site.remlit.blueb.aster.db.table.PolicyTable
import site.remlit.blueb.aster.db.table.RelationshipTable
import site.remlit.blueb.aster.db.table.RoleTable
import site.remlit.blueb.aster.db.table.UserPrivateTable
import site.remlit.blueb.aster.db.table.UserTable
import site.remlit.blueb.aster.exception.MigrationException
import site.remlit.blueb.aster.model.Configuration
import site.remlit.blueb.aster.model.Service
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name
import kotlin.io.path.writer

@OptIn(ExperimentalDatabaseMigrationApi::class)
class MigrationService : Service() {
	companion object {
		private val logger: Logger = LoggerFactory.getLogger(this::class.java)

		private val configuration = Configuration()

		val migrationsDir = Path("src/main/resources/migrations")
		val manifestPath = Path("$migrationsDir/_manifest.txt")

		private val database = Database.connection
		private val dataSource = Database.dataSource

		/**
		 * Creates the database meta table that tracks which migrations have been run if not present.
		 * Also deletes the old migration system if it remains.
		 * */
		fun initialize() {
			/*
			* Why isn't this using Exposed?
			* I felt it would be weird and potentially conflicting if I used Exposed, so instead
			* did this Unnecessary, maybe a little.
			* */
			dataSource.connection.use { conn ->
				val dbMeta = conn.metaData

				if (!dbMeta.getTables(null, null, "database_meta", null).next()) {
					conn.createStatement().use { stmt ->
						stmt.execute("CREATE TABLE database_meta (migration text unique, \"createdAt\" timestamp default CURRENT_TIMESTAMP);")
					}
				}

				if (dbMeta.getTables(null, null, "flyway_schema_history", null).next()) {
					conn.createStatement().use { stmt ->
						stmt.execute("DROP TABLE flyway_schema_history;")
					}
				}
			}
		}

		/**
		 * Generates migration scripts based on current database schema.
		 * */
		fun generate() {
			transaction(database) {
				// todo: automatically look for these
				val tables = listOf(
					AuthTable,
					InviteTable,
					NoteTable,
					NoteLikeTable,
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
						scriptDirectory = migrationsDir.toString(),
						scriptName = "${System.currentTimeMillis()}_Migration_${table.tableName}",
					)
				}
			}

			if (manifestPath.exists()) Files.delete(manifestPath)
			val manifestWriter = Files.createFile(manifestPath).writer()

			for (entry in migrationsDir.listDirectoryEntries().sortedBy { it.name }) {
				if (!entry.name.endsWith(".sql")) continue

				var delete = true

				Files.readAllLines(entry).forEach { line ->
					if (line.isNotBlank()) delete = false
				}

				if (delete) {
					logger.info("Deleting empty script ${entry.name}")
					Files.delete(entry)
				} else {
					manifestWriter.write("${entry.name}\n")
				}
			}

			manifestWriter.flush()
		}

		/**
		 * Determines if a database is up to date or not, and stops server if it isn't
		 * */
		fun isUpToDate() {
			if (getPendingMigrations().isNotEmpty()) {
				logger.error("Theres one or more pending migrations. Please run them before continuing.")
				Runtime.getRuntime().exit(1)
			}
		}

		/**
		 * Gets currently pending migration scripts.
		 *
		 * @return List of migration scripts
		 * */
		fun getPendingMigrations(): List<String> {
			val pending = mutableListOf<String>()

			val resource = this::class.java.classLoader.getResource("migrations/_manifest.txt")
				?: throw MigrationException("Cannot find migration manifest, is the jar malformed?")

			dataSource.connection.use { conn ->
				resource.openStream().bufferedReader(Charsets.UTF_8)
					.readText()
					.lines()
					.forEach { line ->
						if (line.isBlank()) return@forEach
						conn.prepareStatement("SELECT * FROM database_meta WHERE migration = ?").use { stmt ->
							stmt.setString(1, line.replace(".sql", ""))
							stmt.executeQuery().use { rs ->
								if (!rs.next()) pending.add(line)
							}
						}
					}
			}

			return pending
		}

		/**
		 * Executes any pending migrations.
		 * */
		fun execute() {
			initialize()

			val pending = getPendingMigrations()

			dataSource.connection.use { conn ->
				for (pending in pending) {
					if (pending.isBlank()) continue

					val resource = this::class.java.classLoader.getResource("migrations/$pending")
						?: continue

					logger.info("Executing migration $pending...")

					resource.openStream().bufferedReader(Charsets.UTF_8)
						.readText()
						.split(";")
						.forEach { sql ->
							conn.createStatement().use { stmt ->
								// this SQL is safe, it's provided by the jar
								println(sql)

								@Suppress("SqlSourceToSinkFlow")
								stmt.execute(sql)
							}
						}

					conn.prepareStatement("INSERT INTO database_meta (migration) VALUES (?);").use { stmt ->
						stmt.setString(1, pending.replace(".sql", ""))
						stmt.execute()
					}
				}
			}
		}
	}
}
