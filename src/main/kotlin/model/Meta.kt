package site.remlit.blueb.aster.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.neq
import org.jetbrains.exposed.sql.and
import site.remlit.blueb.aster.db.table.UserTable
import site.remlit.blueb.aster.service.NoteService
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
						local = UserService.count(UserTable.host eq null and (UserTable.username neq "instance.actor")),
						remote = UserService.count(UserTable.host neq null),
					),
					notes = MetaStatCount(
						local = NoteService.count(UserTable.host eq null and (UserTable.username neq "instance.actor")),
						remote = NoteService.count(UserTable.host neq null),
					),
				),
				staff = MetaStaff(
					// todo
				)
			)
		}
	}
}
