package site.remlit.blueb.aster.service.ap

import site.remlit.blueb.aster.model.Configuration
import site.remlit.blueb.aster.model.Service

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

/**
 * Service to generate identifiers for ActivityPub objects.
 *
 * @since 2025.5.1.0-SNAPSHOT
 * */
class ApIdService : Service() {
	companion object {
		fun renderBaseApId(): String {
			return Configuration.url.toString()
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
			return if (!id.isNullOrEmpty()) this.renderBaseApId() + "users/" + id + "/inbox" else this.renderBaseApId() + "inbox"
		}


		/**
		 * @param id ID of a User
		 */
		fun renderOutboxApId(id: String? = null): String {
			return if (!id.isNullOrEmpty()) this.renderBaseApId() + "users/" + id + "/outbox" else this.renderBaseApId() + "outbox"
		}

		/**
		 * @param id ID of a User
		 */
		fun renderFollowingApId(id: String): String {
			return this.renderBaseApId() + "users/" + id + "/following"
		}

		/**
		 * @param id ID of a User
		 */
		fun renderFollowersApId(id: String): String {
			return this.renderBaseApId() + "users/" + id + "/followers"
		}
	}
}
