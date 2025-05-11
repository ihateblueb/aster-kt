package me.blueb.model.ap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.blueb.model.LdContext
import me.blueb.model.LdContextItem

@Serializable
abstract class ApObjectWithContext(
    @SerialName("@context")
    val context: List<LdContextItem> = LdContext
)
