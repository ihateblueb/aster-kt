package me.blueb.service

import me.blueb.db.entity.NoteEntity
import me.blueb.db.entity.UserEntity
import me.blueb.db.suspendTransaction
import me.blueb.db.table.NoteTable
import me.blueb.db.table.UserTable
import me.blueb.model.Note
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class NoteService {
	suspend fun get(where: Op<Boolean>): Note? = suspendTransaction {
		val note = NoteEntity
			.find { where }
			.singleOrNull()
			?.apply { // todo: test
				this.load(UserEntity::id)
			}

		if (note != null)
			Note.fromEntity(note)
		else null
	}

	suspend fun getById(id: String): Note? = get(NoteTable.id eq id)
	suspend fun getByApId(apId: String): Note? = get(NoteTable.apId eq apId)

	suspend fun count(where: Op<Boolean>): Long = suspendTransaction {
		NoteTable
			.leftJoin(UserTable)
			.select(where)
			.count()
	}
}
