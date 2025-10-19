package site.remlit.blueb.aster.model.ap

import kotlinx.serialization.Serializable
import site.remlit.blueb.aster.common.model.Note
import site.remlit.blueb.aster.common.model.Visibility
import site.remlit.blueb.aster.service.FormatService
import site.remlit.blueb.aster.service.ap.ApVisibilityService

@Serializable
data class ApNote(
	val id: String,
	val type: ApType.Object = ApType.Object.Note,

	val actor: String,
	val attributedTo: String = actor,

	val summary: String? = null,
	val _misskey_summary: String? = null,

	val content: String? = null,
	val _misskey_content: String? = null,

	val sensitive: Boolean = summary.isNullOrBlank(),

	//val attachment: List<ApDocument>? = null,
	//val tag: List<ApTag>? = null

	val published: String,
	val visibility: Visibility,

	val to: List<String>,
	val cc: List<String>

) : ApObjectWithContext() {
	companion object {
		fun fromEntity(note: Note): ApNote {
			val toCc = ApVisibilityService.visibilityToCc(
				note.visibility,
				followersUrl = null,
				to = note.to
			)

			return ApNote(
				id = note.apId,
				actor = note.user.apId,
				content = note.content,
				_misskey_content = note.content,
				published = FormatService.formatToStandardDateTime(note.createdAt),
				visibility = note.visibility,
				to = toCc["to"]!!,
				cc = toCc["cc"]!!
			)
		}
	}
}
