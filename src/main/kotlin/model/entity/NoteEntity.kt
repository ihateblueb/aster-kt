package me.blueb.model.entity

import kotlinx.serialization.Serializable
import me.blueb.db.NoteTable
import me.blueb.db.UserTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

@Serializable
data class NoteEntity(
    val id: String,
    val apId: String,
    //val user: UserEntity,

    val content: String? = null,

    val to: List<String>? = null,
    val tags: List<String>? = null,
    //val emoji: List<String>? = null,

    val createdAt: String,
    val updatedAt: String,
) {
    companion object {
        fun fromTable(it: ResultRow): NoteEntity {
            return NoteEntity(
                id = it[NoteTable.id].toString(),
                apId = it[NoteTable.apId],
                // user = UserEntity.fromTable(it[NoteTable.user][]), // todo: get join
                content = it[NoteTable.content],
                to = it[NoteTable.to],
                tags = it[NoteTable.tags],
                createdAt = it[NoteTable.createdAt].toString(),
                updatedAt = it[NoteTable.updatedAt].toString(),
            )
        }
    }
}
