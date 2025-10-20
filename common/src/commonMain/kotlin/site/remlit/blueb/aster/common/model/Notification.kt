package site.remlit.blueb.aster.common.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import site.remlit.blueb.aster.common.model.type.NotificationType
import kotlin.js.JsExport

@JsExport
@Serializable
data class Notification(
	val id: String,
	val type: NotificationType,

	val to: User,
	val from: User,

	val note: Note?,
	val relationship: Relationship?,

	val createdAt: LocalDateTime
)