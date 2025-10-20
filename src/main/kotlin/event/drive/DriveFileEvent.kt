package site.remlit.blueb.aster.event.drive

import site.remlit.blueb.aster.common.model.DriveFile
import site.remlit.blueb.aster.event.Event

/**
 * Event related to a drive file.
 *
 * @param driveFile Drive file
 *
 * @since 2025.10.5.0-SNAPSHOT
 * */
abstract class DriveFileEvent(val driveFile: DriveFile) : Event