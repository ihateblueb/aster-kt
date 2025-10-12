package site.remlit.blueb.aster.model.ap.activity

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import site.remlit.blueb.aster.model.ap.ApObjectWithContext
import site.remlit.blueb.aster.model.ap.ApTag
import site.remlit.blueb.aster.model.ap.ApType

@Serializable
data class ApEmojiReactActivity(
	val id: String,
	val type: ApType.Activity = ApType.Activity.EmojiReact,

	val actor: String,
	@Contextual val `object`: Any,

	val emoji: String,
	val tag: List<ApTag> = listOf()
) : ApObjectWithContext()