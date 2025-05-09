package me.blueb.model.repository

import me.blueb.db.UserTable
import me.blueb.db.suspendTransaction
import me.blueb.model.entity.UserEntity
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update

class UserRepository {
    suspend fun get(expression: List<Expression<*>>): UserEntity? = suspendTransaction {
        UserTable
            .select(expression)
            .map { UserEntity.fromTable(it) }
            .singleOrNull()
    }

    suspend fun getById(id: String): UserEntity? = this.get(listOf(UserTable.id eq id))
    suspend fun getByApId(apId: String): UserEntity? = this.get(listOf(UserTable.apId eq apId))

    suspend fun create(user: UserEntity) = suspendTransaction {
        UserTable
            .insert { user } // todo: all null, needs mapping :(
    }

    suspend fun updateById(id: String, user: UserEntity): Int = suspendTransaction {
        UserTable
            .update(
                where = { UserTable.id eq id },
                limit = 1,
                body = { user }
            )
    }
    suspend fun updateByApId(apId: String, user: UserEntity): Int = suspendTransaction {
        UserTable
            .update(
                where = { UserTable.apId eq apId },
                limit = 1,
                body = { user }
            )
    }

    suspend fun deleteById(id: String): Int = suspendTransaction {
        UserTable
            .deleteWhere { UserTable.id eq id }
    }
    suspend fun deleteByApId(apId: String): Int = suspendTransaction {
        UserTable
            .deleteWhere { UserTable.apId eq apId }
    }
}