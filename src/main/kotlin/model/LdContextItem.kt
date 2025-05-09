package me.blueb.model

import kotlinx.serialization.Serializable

@Serializable
sealed class LdContextItem {
    @Serializable
    data class String(
        val value: kotlin.String,
    ) : LdContextItem()

    @Serializable
    data class LdContextDefinitions(
        val value: me.blueb.model.LdContextDefinitions,
    ) : LdContextItem()
}