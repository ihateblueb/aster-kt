package me.blueb.model.ap

import kotlinx.serialization.Serializable
import me.blueb.db.entity.NoteEntity
import me.blueb.db.entity.UserEntity
import me.blueb.db.suspendTransaction
import me.blueb.model.Visibility
import me.blueb.service.ap.ApVisibilityService

private val apVisibilityService = ApVisibilityService()

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
		fun fromEntity(note: NoteEntity) {
			val toCc = apVisibilityService.visibilityToCc(
				note.visibility,
				followersUrl = null,
				to = note.to
			)

			ApNote(
				id = note.apId,
				actor = note.user.apId,
				content = note.content,
				_misskey_content = note.content,
				published = note.createdAt.toString(),
				visibility = note.visibility,
				to = toCc["to"]!!,
				cc = toCc["cc"]!!
			)
		}
	}
}
