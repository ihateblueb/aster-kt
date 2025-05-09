package me.blueb.model.repository

import me.blueb.db.NoteTable
import me.blueb.db.UserPrivateTable
import me.blueb.db.suspendTransaction
import me.blueb.model.activity.ApCreateActivity
import me.blueb.model.entity.NoteEntity
import me.blueb.model.entity.UserPrivateEntity
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update

class NoteRepository {
    suspend fun getById(id: String): NoteEntity? = suspendTransaction {
         NoteTable
            .select(NoteTable.id eq id)
            .map { NoteEntity.fromTable(it) }
            .singleOrNull()
    }
    suspend fun getByApId(apId: String): NoteEntity? = suspendTransaction {
        NoteTable
            .select(NoteTable.apId eq apId)
            .map { NoteEntity.fromTable(it) }
            .singleOrNull()
    }

    suspend fun create(note: NoteEntity) = suspendTransaction {
        NoteTable
            .insert { note } // todo: all null, needs mapping :(
    }

    suspend fun updateById(id: String, note: NoteEntity): Int = suspendTransaction {
        NoteTable
            .update(
                where = { NoteTable.id eq id },
                limit = 1,
                body = { note }
            )
    }
    suspend fun updateByApId(apId: String, note: NoteEntity): Int = suspendTransaction {
        NoteTable
            .update(
                where = { NoteTable.apId eq apId },
                limit = 1,
                body = { note }
            )
    }

    suspend fun deleteById(id: String): Int = suspendTransaction {
        NoteTable
            .deleteWhere { NoteTable.id eq id }
    }
    suspend fun deleteByApId(apId: String): Int = suspendTransaction {
        NoteTable
            .deleteWhere { NoteTable.apId eq apId }
    }
}