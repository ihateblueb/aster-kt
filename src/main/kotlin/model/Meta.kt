package site.remlit.blueb.aster.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.neq
import org.jetbrains.exposed.sql.and
import site.remlit.blueb.aster.db.table.UserTable
import site.remlit.blueb.aster.service.NoteService
import site.remlit.blueb.aster.service.RoleService
import site.remlit.blueb.aster.service.UserService

/*
* TODO: cache
**/
@Serializable
data class Meta(
	val version: MetaVersion,
	val plugins: List<Map<String, String>?> = listOf(),
	val registrations: InstanceRegistrationsType,
	val stats: MetaStats,
	val staff: MetaStaff
) {
	companion object {
		private val packageInformation = PackageInformation()
		private val configuration = Configuration()

		private val userService = UserService()
		private val noteService = NoteService()
		private val roleService = RoleService()

		suspend fun get(): Meta {
			return Meta(
				version = MetaVersion(
					aster = packageInformation.version,
					java = "${System.getProperty("java.vendor")} ${System.getProperty("java.version")}",
					kotlin = KotlinVersion.CURRENT.toString(),
					system = "${System.getProperty("os.name")} ${System.getProperty("os.version")}",
				),
				registrations = configuration.registrations,
				stats = MetaStats(
					users = MetaStatCount(
						local = userService.count(UserTable.host eq null and (UserTable.username neq "instance.actor")),
						remote = userService.count(UserTable.host neq null),
					),
					notes = MetaStatCount(
						local = noteService.count(UserTable.host eq null and (UserTable.username neq "instance.actor")),
						remote = noteService.count(UserTable.host neq null),
					),
				),
				staff = MetaStaff(
					admin = roleService.getUsersOfType(RoleType.Admin),
					mod = roleService.getUsersOfType(RoleType.Mod)
				)
			)
		}
	}
}
