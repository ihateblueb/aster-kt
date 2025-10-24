package site.remlit.aster.common.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
data class Notification(
	val id: String,
	val type: site.remlit.aster.common.model.type.NotificationType,

	val to: site.remlit.aster.common.model.User,
	val from: site.remlit.aster.common.model.User,

	val note: site.remlit.aster.common.model.Note?,
	val relationship: site.remlit.aster.common.model.Relationship?,

	val createdAt: LocalDateTime
)