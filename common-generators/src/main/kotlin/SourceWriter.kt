package site.remlit.aster.common.generator

import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.absolute
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

object SourceWriter {
	val commonMainSource =
		Path("../common/src/commonMain/kotlin/site/remlit/aster/common/model/generated").absolute()

	fun write(path: Path, fileName: String, code: String, ignoredStartLines: Int) {
		val filePath = Path("$path/$fileName.kt")

		Files.createDirectories(filePath.parent)

		var initialText = mutableListOf<String>()
		if (filePath.exists()) initialText = filePath.readText().lines().toMutableList()

		val file = if (filePath.exists()) filePath else Files.createFile(filePath)

		val clippedNew = code.lines().toMutableList()

		repeat(ignoredStartLines) { clippedNew.removeFirst() }
		repeat(ignoredStartLines) { initialText.removeFirst() }

		if (initialText == clippedNew) return

		file.writeText(code)
	}
}