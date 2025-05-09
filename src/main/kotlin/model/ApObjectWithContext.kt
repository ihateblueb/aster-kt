package me.blueb.model

import kotlinx.serialization.SerialName

abstract class ApObjectWithContext {
    @SerialName("@context")
    val context: List<LdContextItem> = LdContext
}
