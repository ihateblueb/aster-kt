package site.remlit.aster.model.ap.activity

import kotlinx.serialization.Serializable
import site.remlit.aster.model.ap.ApIdOrObject
import site.remlit.aster.model.ap.ApObjectWithContext
import site.remlit.aster.model.ap.ApTag
import site.remlit.aster.model.ap.ApType

@Serializable
data class ApEmojiReactActivity(
	val id: String,
	val type: ApType.Activity = ApType.Activity.EmojiReact,

	val actor: String,
	val `object`: ApIdOrObject,

	val emoji: String,
	val tag: List<ApTag> = listOf()
) : ApObjectWithContext()