package me.blueb.model.ap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.blueb.db.entity.UserEntity
import me.blueb.model.Configuration

val configuration = Configuration()

@Serializable
data class ApActor(
	val id: String,
	val type: ApType.Object = ApType.Object.Person,
	val url: String? = null,

	val preferredUsername: String,
	val name: String? = null,

	val icon: ApImage? = null,
	val image: ApImage? = null,

	val summary: String? = null,
	val _misskey_summary: String? = null,

	val sensitive: Boolean = false,
	val discoverable: Boolean = false,
	val manuallyApprovesFollowers: Boolean = false,
	val noindex: Boolean = false,
	val isCat: Boolean = false,
	val speakAsCat: Boolean = false,

	@SerialName("vcard:bday")
	val vcardBday: String? = null,
	@SerialName("vcard:Address")
	val vcardAddress: String? = null,

	val inbox: String,
	val outbox: String? = null,
	val sharedInbox: String,
	// endpoints

	// followers & following

	val publicKey: ApKey

) : ApObjectWithContext() {
    companion object {
        fun fromEntity(user: UserEntity): ApActor =
            ApActor(
                id = user.apId,
                type = ApType.Object.Person,
                preferredUsername = user.username,

				icon = if (user.avatar != null) ApImage(
					src = user.avatar!!,
					sensitive = user.sensitive,
					name = user.avatarAlt,
					alt = user.avatarAlt
				) else null,
				image = if (user.banner != null) ApImage(
					src = user.banner!!,
					sensitive = user.sensitive,
					name = user.bannerAlt,
					alt = user.bannerAlt
				) else null,

                summary = user.bio,
                _misskey_summary = user.bio,

				sensitive = user.sensitive,
				discoverable = user.discoverable,
				manuallyApprovesFollowers = user.locked,
				noindex = !user.indexable,
				isCat = user.isCat,
				speakAsCat = user.isCat,

				vcardBday = user.birthday,
				vcardAddress = user.location,

				inbox = user.inbox,
				outbox = user.outbox,

				sharedInbox = configuration.url.toString() + "inbox",

				publicKey = ApKey(
					id = user.apId + "#main-key",
					owner = user.apId,
					publicKeyPem = user.publicKey
				)
            )
    }
}
