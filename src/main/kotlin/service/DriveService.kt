package site.remlit.blueb.aster.service

import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.dao.load
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import site.remlit.blueb.aster.common.model.DriveFile
import site.remlit.blueb.aster.db.entity.DriveFileEntity
import site.remlit.blueb.aster.db.table.DriveFileTable
import site.remlit.blueb.aster.db.table.UserTable
import site.remlit.blueb.aster.event.drive.DriveFileDeleteEvent
import site.remlit.blueb.aster.model.Service
import site.remlit.blueb.aster.util.model.fromEntities
import site.remlit.blueb.aster.util.model.fromEntity

class DriveService : Service() {
	companion object {
		/**
		 * Get a drive file.
		 *
		 * @param where Query to find drive file
		 *
		 * @return Found drive file, if any
		 * */
		fun get(where: Op<Boolean>): DriveFile? = transaction {
			val driveFile = DriveFileEntity
				.find { where }
				.singleOrNull()
				?.load(DriveFileEntity::user)

			if (driveFile != null)
				DriveFile.fromEntity(driveFile)
			else null
		}

		/**
		 * Get a drive file by ID.
		 *
		 * @param id ID of drive file
		 *
		 * @return Found drive file, if any
		 * */
		fun getById(id: String): DriveFile? = get(DriveFileTable.id eq id)

		/**
		 * Get a drive file by source.
		 *
		 * @param src Source of drive file
		 *
		 * @return Found drive file, if any
		 * */
		fun getBySrc(src: String): DriveFile? = get(DriveFileTable.src eq src)

		/**
		 * Get many drive files.
		 *
		 * @param where Query to find drive files
		 *
		 * @return Found drive files, if any
		 * */
		fun getMany(where: Op<Boolean>, take: Int? = null): List<DriveFile> = transaction {
			val driveFiles = DriveFileEntity
				.find { where }
				.sortedByDescending { it.createdAt }
				.take(take ?: 15)
				.toList()

			if (driveFiles.isEmpty())
				DriveFile.fromEntities(driveFiles)
			else listOf()
		}

		/**
		 * Get count of drive files.
		 *
		 * @param where Query to find drive files
		 *
		 * @return Count of drive files
		 * */
		fun count(where: Op<Boolean>): Long = transaction {
			DriveFileTable
				.leftJoin(UserTable)
				.select(where)
				.count()
		}

		// todo: create method

		/**
		 * Delete a drive file.
		 *
		 * @param where Query to find drive file
		 * */
		fun delete(where: Op<Boolean>) = transaction {
			val entity = DriveFileEntity
				.find { where }
				.singleOrNull()
			if (entity == null) return@transaction

			DriveFileDeleteEvent(DriveFile.fromEntity(entity)).call()
			entity.delete()
		}

		/**
		 * Delete a drive file by ID.
		 *
		 * @param id ID of drive file
		 * */
		fun deleteById(id: String) = delete(DriveFileTable.id eq id)

		/**
		 * Delete a drive file by source.
		 *
		 * @param src Source of drive file
		 * */
		fun deleteBySrc(src: String) = delete(DriveFileTable.src eq src)
	}
}