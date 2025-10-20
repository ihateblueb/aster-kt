package site.remlit.blueb.aster.common.model.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * Federation Policy types determine how a Policy should be enforced
 * */
@JsExport
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
	 * Append content warnings (`cw`) on incoming [site.remlit.blueb.aster.model.Note].
	 * */
	@SerialName("forceContentWarning")
	ForceContentWarning,

	/**
	 * Append sensitive tag on new [site.remlit.blueb.aster.model.User].
	 * */
	@SerialName("forceSensitive")
	ForceSensitive,

	/**
	 * Force all incoming Follow activities to require approval.
	 * */
	@SerialName("forceFollowRequest")
	ForceFollowRequest
}