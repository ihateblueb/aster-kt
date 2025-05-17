package me.blueb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PolicyType {
	// no communication either way
	@SerialName("block")
	Block,

	// quietly ignore
	@SerialName("silence")
	Silence,

	// force content warning on note
	@SerialName("forceContentWarning")
	ForceContentWarning,

	// force sensitive on user
	@SerialName("forceSensitive")
	ForceSensitive,

	// force follows from instance to be accepted
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
