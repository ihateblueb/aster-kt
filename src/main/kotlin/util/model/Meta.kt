package site.remlit.blueb.aster.util.model

import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.neq
import site.remlit.blueb.aster.common.model.Meta
import site.remlit.blueb.aster.common.model.MetaStaff
import site.remlit.blueb.aster.common.model.MetaStatCount
import site.remlit.blueb.aster.common.model.MetaStats
import site.remlit.blueb.aster.common.model.MetaVersion
import site.remlit.blueb.aster.common.model.RoleType
import site.remlit.blueb.aster.db.table.UserTable
import site.remlit.blueb.aster.model.Configuration
import site.remlit.blueb.aster.model.PackageInformation
import site.remlit.blueb.aster.plugin.PluginRegistry
import site.remlit.blueb.aster.service.NoteService
import site.remlit.blueb.aster.service.RoleService
import site.remlit.blueb.aster.service.UserService

fun Meta.Companion.getMeta(): Meta {
	val mods = RoleService.getUsersOfType(RoleType.Mod)
		.toMutableList()

	val admins = RoleService.getUsersOfType(RoleType.Admin)
		.toMutableList()
		.filter { !mods.contains(it) }

	val plugins = mutableMapOf<String, String>()
	PluginRegistry.plugins.forEach { plugins[it.first.name] = it.first.version }

	return Meta(
		name = Configuration.name,
		version = MetaVersion(
			aster = PackageInformation.version,
			java = "${System.getProperty("java.vendor") ?: "Unknown vendor"} ${System.getProperty("java.version") ?: "Unknown version"}",
			kotlin = KotlinVersion.CURRENT.toString(),
			system = "${System.getProperty("os.name") ?: "Unknown name"} ${System.getProperty("os.version") ?: "Unknown version"}",
		),
		plugins = plugins,
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
			admins.toSmall(),
			mods.toSmall(),
		)
	)
}
