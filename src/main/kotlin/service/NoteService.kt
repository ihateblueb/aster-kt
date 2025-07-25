package site.remlit.blueb.aster.service

import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import site.remlit.blueb.aster.db.entity.NoteEntity
import site.remlit.blueb.aster.db.suspendTransaction
import site.remlit.blueb.aster.db.table.NoteTable
import site.remlit.blueb.aster.db.table.UserTable
import site.remlit.blueb.aster.model.Note
import site.remlit.blueb.aster.model.Service

class NoteService : Service() {
	companion object {
		suspend fun get(where: Op<Boolean>): Note? = suspendTransaction {
			val note = NoteEntity
				.find { where }
				.singleOrNull()
				?.load(NoteEntity::user)

			if (note != null)
				Note.fromEntity(note)
			else null
		}

		suspend fun getById(id: String): Note? = get(NoteTable.id eq id)
		suspend fun getByApId(apId: String): Note? = get(NoteTable.apId eq apId)

		suspend fun getMany(where: Op<Boolean>, take: Int? = null): List<Note> = suspendTransaction {
			val notes = NoteEntity
				.find { where }
				.sortedByDescending { it.createdAt }
				.take(take ?: 15)
				.toList()

			if (!notes.isEmpty())
				Note.fromEntities(notes)
			else listOf()
		}

		suspend fun count(where: Op<Boolean>): Long = suspendTransaction {
			NoteTable
				.leftJoin(UserTable)
				.select(where)
				.count()
		}

		suspend fun delete(where: Op<Boolean>) = suspendTransaction {
			NoteEntity
				.find { where }
				.singleOrNull()
				?.delete()
		}

		suspend fun deleteById(id: String) = delete(NoteTable.id eq id)
		suspend fun deleteByApId(apId: String) = delete(NoteTable.id eq apId)
	}
}
