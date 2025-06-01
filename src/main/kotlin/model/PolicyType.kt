package site.remlit.blueb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Federation Policy types determine how a Policy should be enforced
 * */
@Serializable
enum class PolicyType {
	/**
	 * No communication allowed either direction.
	 * */
	@SerialName("block")
	Block,

	/**
	 * Quietly ignore remote host.
	 * */
	@SerialName("silence")
	Silence,

	/**
	 * Append content warnings (`cw`) on incoming [Note].
	 * */
	@SerialName("forceContentWarning")
	ForceContentWarning,

	/**
	 * Append sensitive tag on new [User].
	 * */
	@SerialName("forceSensitive")
	ForceSensitive,

	/**
	 * Force all incoming Follow activities to require approval.
	 * */
	@SerialName("forceFollowRequest")
	ForceFollowRequest;

	companion object {
		fun fromString(value: String): PolicyType {
			return when (value) {
				"block" -> Block
				"silence" -> Silence
				"forceContentWarning" -> ForceContentWarning
				"forceSensitive" -> ForceSensitive
				"forceFollowRequest" -> ForceFollowRequest
				else -> Block
			}
		}
	}
}
