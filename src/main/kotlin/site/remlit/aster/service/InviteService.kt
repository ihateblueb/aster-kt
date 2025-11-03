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
		/**
		 * Get an invitation
		 *
		 * @param where Query to find invite
		 *
		 * @return Found invite, if any
		 * */
		fun get(where: Op<Boolean>): Invite? = transaction {
			val invite = InviteEntity
				.find { where }
				.singleOrNull()
				?.load(NoteEntity::user)

			if (invite != null)
				Invite.fromEntity(invite)
			else null
		}

		/**
		 * Get an invitation by ID
		 *
		 * @param id ID of invite
		 *
		 * @return Found invite, if any
		 * */
		fun getById(id: String): Invite? = get(InviteTable.id eq id)

		/**
		 * Get an invitation by code
		 *
		 * @param code Code of invite
		 *
		 * @return Found invite, if any
		 * */
		fun getByCode(code: String): Invite? = get(InviteTable.code eq code)

		/**
		 * Get many invitations
		 *
		 * @param where Query to find invites
		 *
		 * @return Found invites, if any
		 * */
		fun getMany(where: Op<Boolean>, take: Int? = null): List<InviteEntity> = transaction {
			InviteTable
				.selectAll()
				.where { where }
				.let { InviteEntity.wrapRows(it) }
				.sortedByDescending { it.createdAt }
				.take(take ?: Configuration.timeline.defaultObjects)
				.toList()
		}

		/**
		 * Count invitations
		 *
		 * @param where Query to find invites
		 *
		 * @return Count of invites
		 * */
		fun count(where: Op<Boolean>): Long = transaction {
			InviteTable
				.select(where)
				.count()
		}

		/**
		 * Delete an invitation
		 *
		 * @param where Query to find invite
		 * */
		fun delete(where: Op<Boolean>) = transaction {
			InviteEntity
				.find { where }
				.singleOrNull()
				?.delete()
		}

		/**
		 * Delete an invitation by ID
		 *
		 * @param id ID of invite
		 * */
		fun deleteById(id: String) = delete(InviteTable.id eq id)

		/**
		 * Delete an invitation by code
		 *
		 * @param code Code of invite
		 * */
		fun deleteByCode(code: String) = delete(InviteTable.code eq code)

		/**
		 * Mark an invitation as used
		 *
		 * @param code Code of invite
		 * @param userId ID of the user
		 * */
		fun useInvite(code: String, userId: String) {
			val invite = transaction {
				InviteEntity
					.find { InviteTable.code eq code }
					.singleOrNull()
			}

			if (invite == null)
				throw IllegalArgumentException("Invite does not exist")

			if (invite.user != null)
				throw IllegalArgumentException("Invite has already been used")

			val user = UserService.getById(userId)
				?: throw IllegalArgumentException("User does not exist")

			transaction {
				invite.user = user
				invite.usedAt = TimeService.now()
				invite.flush()
			}
		}
	}
}
