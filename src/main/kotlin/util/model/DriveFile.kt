package site.remlit.blueb.aster.util.model

import site.remlit.blueb.aster.common.model.DriveFile
import site.remlit.blueb.aster.common.model.User
import site.remlit.blueb.aster.db.entity.DriveFileEntity

fun DriveFile.Companion.fromEntity(entity: DriveFileEntity): DriveFile = DriveFile(
	id = entity.id.toString(),
	type = entity.type,
	src = entity.src,
	alt = entity.alt,
	sensitive = entity.sensitive,
	user = User.fromEntity(entity.user),
	createdAt = entity.createdAt,
	updatedAt = entity.updatedAt
)

fun DriveFile.Companion.fromEntities(entities: List<DriveFileEntity>): List<DriveFile> =
	entities.map { fromEntity(it) }