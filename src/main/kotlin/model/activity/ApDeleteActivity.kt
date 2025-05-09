package me.blueb.model.activity

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import me.blueb.model.ApObjectWithContext
import me.blueb.model.ApType

@Serializable
data class ApDeleteActivity(
    val type: ApType.Activity = ApType.Activity.Delete,
    val actor: String? = null,

    @Contextual
    val `object`: Any
) : ApObjectWithContext()