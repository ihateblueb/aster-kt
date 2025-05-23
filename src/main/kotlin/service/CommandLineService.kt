package me.blueb.service

import me.blueb.db.Database
import me.blueb.db.suspendTransaction
import me.blueb.model.PackageInformation

class CommandLineService {
	private val packageInformation = PackageInformation()

	private val userService = UserService()
	private val roleService = RoleService()

	fun help() {
		println("${packageInformation.name} ${packageInformation.version}")
		println("run without arguments to start server")
		println()
		println("help					show this page")
		println("migration:generate			generate migrations (for developer use)")
		println("migration:execute			execute migrations")
		println("role:list				list all roles")
		println("role:create				create a role")
		println("role:give				give role to user")
		println("role:revoke				revoke role from user")
	}

	suspend fun execute(args: Array<String>) {
		println("debug: ${args.joinToString(" ")}")

		Database.database

		if (args.isNotEmpty()) {
			when (args[0]) {
				"help" -> {
					help()
					return
				}

				"migration:generate" -> {
					MigrationService().generate()
					return
				}

				"migration:execute" -> {
					MigrationService().execute()
					return
				}

				"role:list" -> {
					val roles = roleService.getAll()

					println("id	name	type	createdAt	updatedAt")
					for (role in roles) {
						println("${role.id}	${role.name}	${role.type}	${role.createdAt}	${role.updatedAt}")
					}

					println("TODO")
					return
				}

				"role:create" -> {
					println("TODO")
					return
				}

				"role:give" -> {
					val userId = args[1]
					val roleId = args[2]

					if (userId.isNullOrEmpty()) {
						throw Exception("User ID required as second argument")
					}

					if (roleId.isNullOrEmpty()) {
						throw Exception("Role ID required as third argument")
					}

					val user = userService.getById(userId)

					if (user == null) {
						println("User $userId not found")
						return
					}

					val role = roleService.getById(roleId)

					if (role == null) {
						println("Role $roleId not found")
						return
					}

					suspendTransaction {
						val roles = user.roles.toMutableList()
						roles.add(roleId)

						user.roles = roles
						user.storeWrittenValues()
					}

					println("Gave role ${role.name} to ${user.displayName ?: user.username}")
				}

				"role:revoke" -> {
					val userId = args[1]
					val roleId = args[2]

					if (userId.isNullOrEmpty()) {
						throw Exception("User ID required as second argument")
					}

					if (roleId.isNullOrEmpty()) {
						throw Exception("Role ID required as third argument")
					}

					val user = userService.getById(userId)

					if (user == null) {
						println("User $userId not found")
						return
					}

					val role = roleService.getById(roleId)

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

				else -> println("Unknown command ${args[0]}. Run with 'help' for commands.")
			}
		}
	}
}
