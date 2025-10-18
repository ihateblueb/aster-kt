package site.remlit.blueb.aster.common.model

import kotlinx.serialization.Serializable

@Serializable
data class Meta(
	val name: String,
	val version: MetaVersion,
	val plugins: List<Map<String, String>?> = listOf(),
	val registrations: InstanceRegistrationsType,
	val stats: MetaStats,
	val staff: MetaStaff
)