package site.remlit.blueb.aster.event.drive

import site.remlit.blueb.aster.common.model.DriveFile

/**
 * Event for when a drive file is deleted
 *
 * @since 2025.10.5.0-SNAPSHOT
 * */
class DriveFileDeleteEvent(driveFile: DriveFile) : DriveFileEvent(driveFile)
