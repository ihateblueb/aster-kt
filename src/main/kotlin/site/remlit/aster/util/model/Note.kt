package site.remlit.aster.util.model

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import site.remlit.aster.common.model.Note
import site.remlit.aster.common.model.SmallUser
import site.remlit.aster.common.model.User
import site.remlit.aster.db.entity.NoteEntity
import site.remlit.aster.db.entity.NoteLikeEntity
import site.remlit.aster.db.table.NoteLikeTable


fun Note.Companion.fromEntity(entity: NoteEntity): Note {
	return Note(
		id = entity.id.toString(),
		apId = entity.apId,
		conversation = entity.conversation,
		user = User.fromEntity(entity.user),
		replyingTo = null, // todo: fix Note.fromEntity(entity.replyingTo) recursion
		cw = entity.cw,
		content = entity.content,
		visibility = entity.visibility,
		to = entity.to,
		tags = entity.tags,
		repeat = null, // todo: fix Note.fromEntity(entity.repeat) recursion
		createdAt = entity.createdAt,
		updatedAt = entity.updatedAt,
		likes = runBlocking { findLikes(entity.id.toString()) }
	)
}

fun Note.Companion.findLikes(id: String): List<SmallUser> {
	val likes = transaction {
		NoteLikeEntity
			.find { NoteLikeTable.note eq id }
			.sortedByDescending { it.createdAt }
			.toList()
	}

	val users = mutableListOf<SmallUser>()

	likes.forEach {
		users.add(
			SmallUser.fromUser(User.fromEntity(it.user))
		)
	}

	return users.toList()
}

fun Note.Companion.fromEntities(entities: List<NoteEntity>): List<Note> =
	entities.map { fromEntity(it) }