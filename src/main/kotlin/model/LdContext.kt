package me.blueb.model

import kotlinx.serialization.Serializable

val LdContext: List<LdContextItem> =
    listOf(
        LdContextItem.String("https://www.w3.org/ns/activitystreams"),
        LdContextItem.String("https://w3id.org/security/v1"),
        LdContextItem.LdContextDefinitions(LdContextDefinitions()),
    )
