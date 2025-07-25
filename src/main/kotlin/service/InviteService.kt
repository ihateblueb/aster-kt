package site.remlit.blueb.aster.service

import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import site.remlit.blueb.aster.db.entity.InviteEntity
import site.remlit.blueb.aster.db.entity.NoteEntity
import site.remlit.blueb.aster.db.suspendTransaction
import site.remlit.blueb.aster.db.table.InviteTable
import site.remlit.blueb.aster.model.Invite
import site.remlit.blueb.aster.model.Service

class InviteService : Service() {
	companion object {
		suspend fun get(where: Op<Boolean>): Invite? = suspendTransaction {
			val invite = InviteEntity
				.find { where }
				.singleOrNull()
				?.load(NoteEntity::user)

			if (invite != null)
				Invite.fromEntity(invite)
			else null
		}

		suspend fun getById(id: String): Invite? = get(InviteTable.id eq id)
		suspend fun getByCode(code: String): Invite? = get(InviteTable.code eq code)

		suspend fun delete(where: Op<Boolean>) = suspendTransaction {
			InviteEntity
				.find { where }
				.singleOrNull()
				?.delete()
		}

		suspend fun deleteById(id: String) = delete(InviteTable.id eq id)
		suspend fun deleteByCode(code: String) = delete(InviteTable.code eq code)

		suspend fun useInvite(code: String, userId: String) {
			val invite = suspendTransaction {
				InviteEntity
					.find { InviteTable.code eq code }
					.singleOrNull()
			}

			if (invite == null)
				throw IllegalArgumentException("Invite does not exist")

			val user = UserService.getById(userId)

			if (user == null)
				throw IllegalArgumentException("User does not exist")

			suspendTransaction {
				invite.user = user
				invite.usedAt = TimeService.now()
				invite.flush()
			}
		}
	}
}
