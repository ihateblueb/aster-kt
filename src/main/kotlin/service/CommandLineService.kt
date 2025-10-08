package site.remlit.blueb.aster.service

import org.slf4j.LoggerFactory
import site.remlit.blueb.aster.db.Database
import site.remlit.blueb.aster.db.entity.InviteEntity
import site.remlit.blueb.aster.db.suspendTransaction
import site.remlit.blueb.aster.model.PackageInformation
import site.remlit.blueb.aster.model.Service
import java.util.*

class CommandLineService : Service() {
	companion object {
		private val logger = LoggerFactory.getLogger(this::class.java)

		private val packageInformation = PackageInformation()

		fun help() {
			logger.info("${packageInformation.name} ${packageInformation.version}")
			logger.info("Run without arguments to start server")
			logger.info("help					Show this page")
			logger.info("migration:generate			Generate migrations (for developer use)")
			logger.info("migration:execute			Execute migrations")
			logger.info("role:list				List all roles")
			logger.info("role:create				Create a role")
			logger.info("role:give				Give role to user")
			logger.info("role:revoke				Revoke role from user")
			logger.info("invite:generate			Generate invite")
		}

		suspend fun prompt() {
			val scanner = Scanner(System.`in`)
			val line = scanner.nextLine()

			logger.info("Starting prompt.")

			when (line) {
				"exit" -> scanner.close()
				else -> execute(line.split(" ").toTypedArray())
			}

			scanner.close()
		}

		suspend fun execute(args: Array<String>) {
			println("debug: ${args.joinToString(" ")}")

			Database.connection

			if (args.isNotEmpty()) {
				when (args[0]) {
					"help" -> {
						help()
						return
					}

					"prompt" -> {
						prompt()
						return
					}

					"migration:generate" -> {
						MigrationService.generate()
						return
					}

					"migration:execute" -> {
						MigrationService.execute()
						return
					}

					"role:list" -> {
						val roles = RoleService.getAll()

						for (role in roles) {
							println("${role.id}	${role.name}	(${role.type})	${role.createdAt}	${role.updatedAt}")
						}
						return
					}

					"role:create" -> {
						println("TODO")
						return
					}

					"role:give" -> {
						val userId = args[1]
						val roleId = args[2]

						if (userId.isEmpty()) {
							throw Exception("User ID required as second argument")
						}

						if (roleId.isEmpty()) {
							throw Exception("Role ID required as third argument")
						}

						val user = UserService.getById(userId)

						if (user == null) {
							println("User $userId not found")
							return
						}

						val role = RoleService.getById(roleId)

						if (role == null) {
							println("Role $roleId not found")
							return
						}

						suspendTransaction {
							val roles = user.roles.toMutableList()
							roles.add(roleId)

							user.roles = roles.toList()
							user.flush()
						}

						println("Gave role ${role.name} to ${user.displayName ?: user.username}")
					}

					"role:revoke" -> {
						val userId = args[1]
						val roleId = args[2]

						if (userId.isEmpty()) {
							throw Exception("User ID required as second argument")
						}

						if (roleId.isEmpty()) {
							throw Exception("Role ID required as third argument")
						}

						val user = UserService.getById(userId)

						if (user == null) {
							println("User $userId not found")
							return
						}

						val role = RoleService.getById(roleId)

						if (role == null) {
							println("Role $roleId not found")
							return
						}

						suspendTransaction {
							val roles = user.roles.toMutableList()
							roles.remove(roleId)

							user.roles = roles
							user.storeWrittenValues()
						}

						println("Revoked role ${role.name} from ${user.displayName ?: user.username}")
					}

					"invite:generate" -> {
						val instanceActor = UserService.getInstanceActor()

						val id = IdentifierService.generate()
						val code = RandomService.generateString()

						suspendTransaction {
							InviteEntity.new(id) {
								this.code = code
								creator = instanceActor
							}
						}

						println("Created new invite: $code")
					}

					else -> println("Unknown command ${args[0]}. Run with 'help' for commands.")
				}
			}
		}
	}
}
