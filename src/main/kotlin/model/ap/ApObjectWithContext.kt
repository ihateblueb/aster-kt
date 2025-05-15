package me.blueb.model.ap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import me.blueb.model.LdContext

@Serializable
abstract class ApObjectWithContext(
    @SerialName("@context")
    val context: JsonArray = LdContext
)
