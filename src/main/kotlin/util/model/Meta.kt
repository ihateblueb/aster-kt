package site.remlit.blueb.aster.util.model

import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.neq
import site.remlit.blueb.aster.common.model.Meta
import site.remlit.blueb.aster.common.model.MetaStaff
import site.remlit.blueb.aster.common.model.MetaStatCount
import site.remlit.blueb.aster.common.model.MetaStats
import site.remlit.blueb.aster.common.model.MetaVersion
import site.remlit.blueb.aster.db.table.UserTable
import site.remlit.blueb.aster.model.Configuration
import site.remlit.blueb.aster.model.PackageInformation
import site.remlit.blueb.aster.service.NoteService
import site.remlit.blueb.aster.service.UserService

fun Meta.Companion.get(): Meta {
	return Meta(
		name = Configuration.name,
		version = MetaVersion(
			aster = PackageInformation.version,
			java = "${System.getProperty("java.vendor")} ${System.getProperty("java.version")}",
			kotlin = KotlinVersion.CURRENT.toString(),
			system = "${System.getProperty("os.name")} ${System.getProperty("os.version")}",
		),
		registrations = Configuration.registrations,
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