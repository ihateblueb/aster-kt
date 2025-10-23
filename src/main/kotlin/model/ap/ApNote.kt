package site.remlit.blueb.aster.model.ap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import site.remlit.blueb.aster.common.model.Note
import site.remlit.blueb.aster.common.model.Visibility
import site.remlit.blueb.aster.service.FormatService
import site.remlit.blueb.aster.service.ap.ApVisibilityService

@Serializable
data class ApNote(
	val id: String,
	val type: ApType.Object = ApType.Object.Note,

	val attributedTo: String,

	val summary: String? = null,
	@SerialName("_misskey_summary")
	val misskeySummary: String? = null,

	val content: String? = null,
	@SerialName("_misskey_content")
	val misskeyContent: String? = null,

	val sensitive: Boolean = summary.isNullOrBlank(),

	//val attachment: List<ApDocument>? = null,
	//val tag: List<ApTag>? = null

	val published: String,
	val visibility: Visibility? = null,

	val to: List<String>,
	val cc: List<String>

) : ApObject {
	companion object {
		fun fromEntity(note: Note): ApNote {
			val toCc = ApVisibilityService.visibilityToCc(
				note.visibility,
				followersUrl = null,
				to = note.to
			)

			return ApNote(
				id = note.apId,
				attributedTo = note.user.apId,
				content = note.content,
				misskeyContent = note.content,
				published = FormatService.formatToStandardDateTime(note.createdAt),
				visibility = note.visibility,
				to = toCc["to"]!!,
				cc = toCc["cc"]!!
			)
		}
	}
}
