package site.remlit.aster.service

import org.jetbrains.exposed.v1.core.JoinType
import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.alias
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.dao.load
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import site.remlit.aster.common.model.Note
import site.remlit.aster.common.model.Notification
import site.remlit.aster.common.model.Relationship
import site.remlit.aster.common.model.type.NotificationType
import site.remlit.aster.db.entity.NoteEntity
import site.remlit.aster.db.entity.NotificationEntity
import site.remlit.aster.db.entity.RelationshipEntity
import site.remlit.aster.db.entity.UserEntity
import site.remlit.aster.db.table.NoteTable
import site.remlit.aster.db.table.NotificationTable
import site.remlit.aster.db.table.RelationshipTable
import site.remlit.aster.db.table.UserTable
import site.remlit.aster.model.Configuration
import site.remlit.aster.model.Service
import site.remlit.aster.util.model.fromEntities
import site.remlit.aster.util.model.fromEntity

class NotificationService : Service() {
	companion object {
		fun get(where: Op<Boolean>): Notification? = transaction {
			val entity = NotificationEntity
				.find { where }
				.singleOrNull()
				?.load(
					NotificationEntity::to,
					NotificationEntity::from,
					NotificationEntity::note,
					NotificationEntity::relationship
				)

			if (entity != null)
				Notification.fromEntity(entity)
			else null
		}

		fun getById(id: String): Notification? = get(NotificationTable.id eq id)

		/**
		 * Reference the "to" user on a notification.
		 * For usage in queries.
		 * */
		val userToAlias = UserTable.alias("to")

		/**
		 * Reference the "from" user on a notification.
		 * For usage in queries.
		 * */
		val userFromAlias = UserTable.alias("from")

		fun getMany(where: Op<Boolean>, take: Int? = null): List<Notification> = transaction {
			val entities = NotificationTable
				.join(userToAlias, JoinType.INNER, NotificationTable.to, userToAlias[UserTable.id])
				.join(userFromAlias, JoinType.INNER, NotificationTable.from, userFromAlias[UserTable.id])
				.join(NoteTable, JoinType.LEFT, NotificationTable.note, NoteTable.id)
				.join(RelationshipTable, JoinType.LEFT, NotificationTable.relationship, RelationshipTable.id)
				.selectAll()
				.where { where }
				.let { NotificationEntity.wrapRows(it) }
				.sortedByDescending { it.createdAt }
				.take(take ?: Configuration.timeline.defaultObjects)
				.toList()

			if (!entities.isEmpty())
				Notification.fromEntities(entities)
			else listOf()
		}

		fun create(
			type: NotificationType,
			to: UserEntity,
			from: UserEntity,
			note: NoteEntity?,
			relationship: RelationshipEntity?
		) {
			val id = IdentifierService.generate()
			transaction {
				NotificationEntity.new(id) {
					this.type = type
					this.to = to
					this.from = from
					if (note != null)
						this.note = note
					if (relationship != null)
						this.relationship = relationship
				}
			}
		}

		fun create(
			type: NotificationType,
			to: UserEntity,
			from: UserEntity,
			note: Note?,
			relationship: RelationshipEntity?
		) = create(
			type,
			to,
			from,
			if (note != null) NoteEntity[note.id] else null,
			relationship
		)

		fun create(
			type: NotificationType,
			to: UserEntity,
			from: UserEntity,
			note: NoteEntity?,
			relationship: Relationship?
		) = create(
			type,
			to,
			from,
			note,
			if (relationship != null) RelationshipEntity[relationship.id] else null
		)

		fun create(
			type: NotificationType,
			to: UserEntity,
			from: UserEntity,
			note: Note?,
			relationship: Relationship?
		) = create(
			type,
			to,
			from,
			if (note != null) NoteEntity[note.id] else null,
			if (relationship != null) RelationshipEntity[relationship.id] else null
		)

		fun create(
			type: NotificationType,
			to: UserEntity,
			from: UserEntity,
			note: Note?,
		) = create(
			type,
			to,
			from,
			if (note != null) NoteEntity[note.id] else null,
			null as RelationshipEntity?
		)

		fun create(
			type: NotificationType,
			to: UserEntity,
			from: UserEntity
		) = create(
			type,
			to,
			from,
			null as NoteEntity?,
			null as RelationshipEntity?
		)

		fun bite(
			to: UserEntity,
			from: UserEntity,
			note: Note? = null,
		) = create(NotificationType.Bite, to, from, note)
	}
}