package me.blueb.model.repository

import me.blueb.db.UserPrivateTable
import me.blueb.db.suspendTransaction
import me.blueb.model.entity.UserPrivateEntity
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update

class UserPrivateRepository {
    suspend fun getById(id: String): UserPrivateEntity? = suspendTransaction {
        UserPrivateTable
            .select(UserPrivateTable.id eq id)
            .map { UserPrivateEntity.fromTable(it) }
            .singleOrNull()
    }

    suspend fun create(userPrivate: UserPrivateEntity) = suspendTransaction {
        UserPrivateTable
            .insert { userPrivate } // todo: all null, needs mapping :(
    }

    suspend fun updateById(id: String, userPrivate: UserPrivateEntity): Int = suspendTransaction {
        UserPrivateTable
            .update(
                where = { UserPrivateTable.id eq id },
                limit = 1,
                body = { userPrivate }
            )
    }

    suspend fun deleteById(id: String): Int = suspendTransaction {
        UserPrivateTable
            .deleteWhere { UserPrivateTable.id eq id }
    }
}