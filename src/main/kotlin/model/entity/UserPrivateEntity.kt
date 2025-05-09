package me.blueb.model.entity

import kotlinx.serialization.Serializable
import me.blueb.db.UserPrivateTable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class UserPrivateEntity(
    val id: String = "",
    val password: String = "",
) {
    companion object {
        fun fromTable(it: ResultRow): UserPrivateEntity {
            return UserPrivateEntity(
                id = it[UserPrivateTable.id].toString(),
                password = it[UserPrivateTable.password],
            )
        }
    }
}
