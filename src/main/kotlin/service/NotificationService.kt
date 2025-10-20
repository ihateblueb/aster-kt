package site.remlit.blueb.aster.service

import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.dao.load
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import site.remlit.blueb.aster.common.model.Notification
import site.remlit.blueb.aster.db.entity.NotificationEntity
import site.remlit.blueb.aster.db.table.NotificationTable
import site.remlit.blueb.aster.db.table.UserTable
import site.remlit.blueb.aster.model.Configuration
import site.remlit.blueb.aster.model.Service
import site.remlit.blueb.aster.util.model.fromEntities
import site.remlit.blueb.aster.util.model.fromEntity

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

		fun getMany(where: Op<Boolean>, take: Int? = null): List<Notification> = transaction {
			val entities = (NotificationTable leftJoin UserTable)
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
	}
}