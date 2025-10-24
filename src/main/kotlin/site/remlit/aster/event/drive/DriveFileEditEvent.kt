package site.remlit.aster.event.drive

import site.remlit.aster.common.model.DriveFile

/**
 * Event for when a drive file is edited
 *
 * @since 2025.10.5.0-SNAPSHOT
 * */
class DriveFileEditEvent(driveFile: DriveFile) : DriveFileEvent(driveFile)
