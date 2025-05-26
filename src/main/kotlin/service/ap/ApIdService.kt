package me.blueb.service.ap

import me.blueb.model.Configuration

/*
* Changing these can be bad!
*
* ActivityPub requires IDs to be constant, they cannot be changed!
* This service makes sure that the specific types of AP IDs generated
* across aster are consistent. Changing them can cause federation issues.
*
* Only modify this service if adding a new type of AP ID or adding extra
* functionality to an existing one that doesn't affect the previous
* use case.
* */

class ApIdService {
	private val configuration = Configuration()

	fun renderBaseApId(): String {
		return configuration.url.toString()
	}

	/**
	 * @param id ID of a Note
	 */
	fun renderNoteApId(id: String): String {
		return this.renderBaseApId() + "notes/" + id
	}

	/**
	 * @param id ID of a User
	 */
	fun renderUserApId(id: String): String {
		return this.renderBaseApId() + "users/" + id
	}

	/**
	 * @param id ID of a User
	 */
	fun renderInboxApId(id: String? = null): String {
		return if (id.isNullOrEmpty()) this.renderBaseApId() + "users/" + id + "/inbox" else this.renderBaseApId() + "inbox"
	}


	/**
	 * @param id ID of a User
	 */
	fun renderOutboxApId(id: String? = null): String {
		return if (id.isNullOrEmpty()) this.renderBaseApId() + "users/" + id + "/outbox" else this.renderBaseApId() + "outbox"
	}
}
