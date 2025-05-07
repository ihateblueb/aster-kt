package me.blueb.models

import kotlinx.serialization.Serializable

@Serializable
sealed class LdContextItem {
    @Serializable
    data class String(val value: kotlin.String) : LdContextItem()

    @Serializable
    data class LdContext(val value: me.blueb.models.LdContext) : LdContextItem()
}

val LdContextList: List<LdContextItem> = listOf(
    LdContextItem.String("https://www.w3.org/ns/activitystreams"),
    LdContextItem.String("https://w3id.org/security/v1"),
    LdContextItem.LdContext(LdContext())
)