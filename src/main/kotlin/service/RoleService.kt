package site.remlit.blueb.aster.service

import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.json.contains
import site.remlit.blueb.aster.db.entity.RoleEntity
import site.remlit.blueb.aster.db.suspendTransaction
import site.remlit.blueb.aster.db.table.RoleTable
import site.remlit.blueb.aster.db.table.UserTable
import site.remlit.blueb.aster.model.RoleType
import site.remlit.blueb.aster.model.Service
import site.remlit.blueb.aster.model.User

class RoleService : Service() {
	companion object {
		suspend fun get(where: Op<Boolean>): RoleEntity? = suspendTransaction {
			RoleEntity
				.find { where }
				.singleOrNull()
		}

		suspend fun getMany(where: Op<Boolean>): List<RoleEntity> = suspendTransaction {
			RoleEntity
				.find { where }
				.toList()
		}

		suspend fun getAll(): List<RoleEntity> = suspendTransaction {
			RoleEntity
				.all()
				.toList()
		}

		suspend fun getById(id: String): RoleEntity? = get(RoleTable.id eq id)

		suspend fun userHasRoleOfType(userId: String, type: RoleType): Boolean {
			val user = UserService.getById(userId)
			val rolesOfType = getMany(RoleTable.type eq type)

			if (user != null && rolesOfType.any { it.id.toString() in user.roles })
				println("id in user roles")

			if (user != null && rolesOfType.any { it.id.toString() in user.roles })
				return true

			return false
		}

		suspend fun getUsersOfType(type: RoleType): List<User?> {
			val rolesOfType = getMany(RoleTable.type eq type)

			val usersOfType = mutableListOf<User>()

			for (role in rolesOfType) {
				UserService.getMany(UserTable.roles.contains(role)).forEach { userEntity ->
					val user = User.fromEntity(userEntity)

					if (!usersOfType.contains(user))
						usersOfType.add(user)
				}
			}

			return usersOfType.toList()
		}
	}
}
