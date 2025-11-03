package site.remlit.aster.service.ap

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import site.remlit.aster.model.Service

/**
 * Service with extra utilities for handling ActivityPub objects.
 *
 * @since 2025.5.1.0-SNAPSHOT
 * */
object ApUtilityService : Service {
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

	fun extractArray(item: JsonElement?): JsonArray? = item as? JsonArray
	fun extractObject(item: JsonElement?): JsonObject? = item?.jsonObject

	fun byteArrayToObject(byteArray: ByteArray): JsonObject? = try {
		Json.parseToJsonElement(String(byteArray)).jsonObject
	} catch (e: SerializationException) {
		null
	}
}
