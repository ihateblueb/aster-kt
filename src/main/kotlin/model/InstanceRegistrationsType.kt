package site.remlit.blueb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class InstanceRegistrationsType {
	@SerialName("open")
	Open,

	@SerialName("approval")
	Approval,

	@SerialName("invite")
	Invite,

	@SerialName("closed")
	Closed
}
