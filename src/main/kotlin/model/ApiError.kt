package me.blueb.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
    val message: String? = null,
    val requestId: String? = null
)