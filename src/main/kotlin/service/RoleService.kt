package site.remlit.blueb.aster.service

import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.json.contains
import site.remlit.blueb.aster.db.entity.RoleEntity
import site.remlit.blueb.aster.db.table.RoleTable
import site.remlit.blueb.aster.db.table.UserTable
import site.remlit.blueb.aster.model.RoleType
import site.remlit.blueb.aster.model.Service
import site.remlit.blueb.aster.model.User

/**
 * Service for managing user roles.
 *
 * @since 2025.5.1.0-SNAPSHOT
 * */
class RoleService : Service() {
	companion object {
		/**
		 * Get a role.
		 *
		 * @param where Query to find role
		 *
		 * @return Found role, if any
		 * */
		fun get(where: Op<Boolean>): RoleEntity? = transaction {
			RoleEntity
				.find { where }
				.singleOrNull()
		}

		/**
		 * Get a list of roles.
		 *
		 * @param where Query to find roles
		 *
		 * @return List of found roles
		 * */
		fun getMany(where: Op<Boolean>): List<RoleEntity> = transaction {
			RoleEntity
				.find { where }
				.toList()
		}

		/**
		 * Get a list of every role
		 *
		 * @return List of roles
		 * */
		fun getAll(): List<RoleEntity> = transaction {
			RoleEntity
				.all()
				.toList()
		}

		/**
		 * Get a role by ID.
		 *
		 * @param id ID of role
		 *
		 * @return Found role, if any
		 * */
		fun getById(id: String): RoleEntity? = get(RoleTable.id eq id)

		/**
		 * Gets the highest role type for a user
		 *
		 * @param userId ID of the user to check
		 *
		 * @return Highest tole type
		 * */
		fun getUserHighestRole(userId: String): RoleType? {
			val user = UserService.getById(userId) ?: return null
			val rolesOfType = getMany(RoleTable.id inList user.roles)

			if (rolesOfType.any { it.type == RoleType.Admin }) return RoleType.Admin
			if (rolesOfType.any { it.type == RoleType.Mod }) return RoleType.Mod
			return RoleType.Normal
		}

		/**
		 * Determines if a user has a role matching a role type
		 *
		 * @param userId ID of the user to check
		 * @param type Type of role
		 *
		 * @return If the user has the role or not
		 * */
		fun userHasRoleOfType(userId: String, type: RoleType): Boolean {
			val user = UserService.getById(userId)
			val rolesOfType = getMany(RoleTable.type eq type)

			if (user != null && rolesOfType.any { it.id.toString() in user.roles })
				println("id in user roles")

			if (user != null && rolesOfType.any { it.id.toString() in user.roles })
				return true

			return false
		}

		/**
		 * Gets a list of users with a role type
		 *
		 * @param type Type of role
		 *
		 * @return List of users with a role of a certain type
		 * */
		fun getUsersOfType(type: RoleType): List<User> {
			val rolesOfType = getMany(RoleTable.type eq type)

			val usersOfType = mutableListOf<User>()

			for (role in rolesOfType) {
				UserService.getMany(UserTable.roles.contains(role.id)).forEach { userEntity ->
					val user = User.fromEntity(userEntity)

					if (!usersOfType.contains(user))
						usersOfType.add(user)
				}
			}

			return usersOfType.toList()
		}
	}
}
