package me.blueb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ResolveAcceptType {
	/**
	 * Expect response of regular JSON
	 * */
	@SerialName("application/json")
	json,

	/**
	 * Expect response of Activity JSON
	 * Use for ActivityPub objects
	 * */
	@SerialName("application/activity+json")
	activityJson,


	/**
	 * Expect response of JRD JSON
	 * Use for NodeInfo, Webfinger, etc.
	 * */
	@SerialName("application/jrd+json")
	jrdJson
}
