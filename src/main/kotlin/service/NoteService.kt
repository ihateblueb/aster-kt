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
import org.jetbrains.exposed.sql.deleteWhere

class NoteService {
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
