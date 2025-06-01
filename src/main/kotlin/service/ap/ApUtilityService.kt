package me.blueb.service.ap

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class ApUtilityService {
	fun extractString(item: JsonElement?): String? = when (item) {
		is JsonArray -> item.first().jsonPrimitive.toString()
		is JsonPrimitive -> item.jsonPrimitive.contentOrNull
		else -> null
	}

	fun extractBoolean(item: JsonElement?): Boolean? = when (item) {
		is JsonArray -> item.first().jsonPrimitive.booleanOrNull
		is JsonPrimitive -> item.jsonPrimitive.booleanOrNull
		else -> null
	}

	fun extractObject(item: JsonElement?): JsonObject? = item?.jsonObject
}
