package site.remlit.blueb.aster.model.ap.activity

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive
import site.remlit.blueb.aster.model.ap.ApObjectWithContext
import site.remlit.blueb.aster.model.ap.ApTag
import site.remlit.blueb.aster.model.ap.ApType

@Serializable
data class ApEmojiReactActivity(
	val id: String,
	val type: ApType.Activity = ApType.Activity.EmojiReact,

	val actor: String,
	val `object`: JsonPrimitive,

	val emoji: String,
	val tag: List<ApTag> = listOf()
) : ApObjectWithContext()