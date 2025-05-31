package me.blueb.service.ap

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

class ApUtilityService {
	fun extractString(item: JsonElement): String? = when (item) {
		is JsonArray -> item.first().toString()
		is JsonPrimitive -> item.toString()
		else -> null
	}
}
