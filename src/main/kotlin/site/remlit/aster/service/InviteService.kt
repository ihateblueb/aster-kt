package site.remlit.aster.service

import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.dao.load
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import site.remlit.aster.common.model.Invite
import site.remlit.aster.db.entity.InviteEntity
import site.remlit.aster.db.entity.NoteEntity
import site.remlit.aster.db.table.InviteTable
import site.remlit.aster.model.Configuration
import site.remlit.aster.model.Service
import site.remlit.aster.util.model.fromEntity

/**
 * Service for managing user invites.
 *
 * @since 2025.5.1.0-SNAPSHOT
 * */
class InviteService : Service() {
	companion object {
		fun get(where: Op<Boolean>): Invite? = transaction {
			val invite = InviteEntity
				.find { where }
				.singleOrNull()
				?.load(NoteEntity::user)

			if (invite != null)
				Invite.fromEntity(invite)
			else null
		}

		fun getById(id: String): Invite? = get(InviteTable.id eq id)
		fun getByCode(code: String): Invite? = get(InviteTable.code eq code)

		fun getMany(where: Op<Boolean>, take: Int? = null): List<InviteEntity> = transaction {
			InviteTable
				.selectAll()
				.where { where }
				.let { InviteEntity.wrapRows(it) }
				.sortedByDescending { it.createdAt }
				.take(take ?: Configuration.timeline.defaultObjects)
				.toList()
		}

		fun count(where: Op<Boolean>): Long = transaction {
			InviteTable
				.select(where)
				.count()
		}

		fun delete(where: Op<Boolean>) = transaction {
			InviteEntity
				.find { where }
				.singleOrNull()
				?.delete()
		}

		fun deleteById(id: String) = delete(InviteTable.id eq id)
		fun deleteByCode(code: String) = delete(InviteTable.code eq code)

		fun useInvite(code: String, userId: String) {
			val invite = transaction {
				InviteEntity
					.find { InviteTable.code eq code }
					.singleOrNull()
			}

			if (invite == null)
				throw IllegalArgumentException("Invite does not exist")

			val user = UserService.getById(userId) ?: throw IllegalArgumentException("User does not exist")

			transaction {
				invite.user = user
				invite.usedAt = TimeService.now()
				invite.flush()
			}
		}
	}
}
