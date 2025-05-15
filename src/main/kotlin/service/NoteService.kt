package me.blueb.service

import me.blueb.db.entity.NoteEntity
import me.blueb.db.suspendTransaction
import me.blueb.db.table.NoteTable
import me.blueb.db.table.UserTable
import org.jetbrains.exposed.sql.Op

class NoteService {
	suspend fun get(where: Op<Boolean>): NoteEntity? = suspendTransaction {
		NoteEntity
			.find { where }
			.singleOrNull()
	}

	suspend fun count(where: Op<Boolean>): Long = suspendTransaction {
		NoteTable
			.leftJoin(UserTable)
			.select(where)
			.count()
	}
}
