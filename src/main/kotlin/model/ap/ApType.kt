package site.remlit.blueb.aster.model.ap

import kotlinx.serialization.Serializable

@Serializable
class ApType {
	enum class Activity {
		Accept,
		Add,
		Announce,
		Bite,
		Block,
		Create,
		Delete,
		EmojiReact,
		Follow,
		Like,
		Reject,
		Remove,
		Undo,
		Update
	}

	enum class Object {
		Person,
		Service,
		Note,
		Image,
		Key,
	}
}
