package me.blueb.service

import me.blueb.db.entity.UserEntity
import me.blueb.db.suspendTransaction
import me.blueb.db.table.UserTable
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import kotlin.text.get

class UserService() {
    suspend fun get(where: List<Expression<*>>): UserEntity? = suspendTransaction {
        UserTable
            .select(where)
            .map { UserEntity.wrapRow(it) }
            .singleOrNull()
    }

    suspend fun getById(id: String): UserEntity? = suspendTransaction { UserEntity.get(id) }
    suspend fun getByApId(apId: String): UserEntity? = this.get(listOf(UserTable.apId eq apId))
}