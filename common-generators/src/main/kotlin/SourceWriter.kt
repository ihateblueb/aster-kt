package site.remlit.blueb.aster.common.generator

import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.absolute
import kotlin.io.path.exists
import kotlin.io.path.writeText

object SourceWriter {
	val commonMainSource =
		Path("../common/src/commonMain/kotlin/site/remlit/blueb/aster/common/model/generated").absolute()

	fun write(path: Path, fileName: String, code: String) {
		val filePath = Path("$path/$fileName.kt")

		Files.createDirectories(filePath.parent)
		val file = if (filePath.exists()) filePath else Files.createFile(filePath)

		file.writeText(code)
	}
}