package me.blueb.db.entity

import me.blueb.db.table.NoteTable
import me.blueb.db.table.UserTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class NoteEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, NoteEntity>(NoteTable)

    var apId by NoteTable.apId
    var user by UserEntity referencedOn NoteTable.user

    var content by NoteTable.content

    var to by NoteTable.to
    var tags by NoteTable.tags

    var createdAt by NoteTable.createdAt
    var updatedAt by NoteTable.updatedAt
}
