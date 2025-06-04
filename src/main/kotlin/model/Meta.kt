package site.remlit.blueb.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.neq
import org.jetbrains.exposed.sql.and
import site.remlit.blueb.db.table.UserTable
import site.remlit.blueb.service.NoteService
import site.remlit.blueb.service.UserService

@Serializable
data class Meta(
	val version: MetaVersion,
	val plugins: List<Map<String, String>?> = listOf(),
	val stats: MetaStats,
	val staff: MetaStaff
) {
	companion object {
		private val packageInformation = PackageInformation()

		private val userService = UserService()
		private val noteService = NoteService()

		suspend fun get(): Meta {
			return Meta(
				version = MetaVersion(
					aster = packageInformation.version,
					java = "${System.getProperty("java.vendor")} ${System.getProperty("java.version")}",
					kotlin = KotlinVersion.CURRENT.toString(),
					system = "${System.getProperty("os.name")} ${System.getProperty("os.version")}",
				),
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
					// todo
				)
			)
		}
	}
}
