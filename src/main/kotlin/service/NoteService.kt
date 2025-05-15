package me.blueb.service

import me.blueb.db.entity.NoteEntity
import me.blueb.db.entity.UserEntity
import me.blueb.db.suspendTransaction
import me.blueb.db.table.NoteTable
import me.blueb.db.table.UserTable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class NoteService {
	suspend fun get(where: Op<Boolean>): NoteEntity? = suspendTransaction {
		NoteEntity
			.find { where }
			.singleOrNull()
	}

	suspend fun getById(id: String): NoteEntity? = get(NoteTable.id eq id)
	suspend fun getByApId(apId: String): NoteEntity? = get(NoteTable.apId eq apId)

	suspend fun count(where: Op<Boolean>): Long = suspendTransaction {
		NoteTable
			.leftJoin(UserTable)
			.select(where)
			.count()
	}
}
