import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import site.remlit.blueb.aster.db.table.NoteLikeTable

class NoteLikeEntity(id: EntityID<String>) : Entity<String>(id) {
	companion object : EntityClass<String, NoteLikeEntity>(NoteLikeTable)

	var user by UserEntity referencedOn NoteLikeTable.user
	var note by NoteEntity referencedOn NoteLikeTable.note
	var createdAt by NoteLikeTable.createdAt
}

