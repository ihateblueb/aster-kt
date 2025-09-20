package site.remlit.blueb.aster.db.type

import org.ktorm.schema.BaseTable
import org.ktorm.schema.SqlType
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

class DatabaseList : SqlType<List<String>>(Types.ARRAY, "list") {
	override fun doGetResult(rs: ResultSet, index: Int): List<String> {
		val retrievedData = rs.getArray(index)

		val list = (retrievedData.array as Array<String>).toList()

		return when {
			retrievedData == null -> emptyList()
			else -> list
		}
	}

	override fun doSetParameter(ps: PreparedStatement, index: Int, parameter: List<String>) {
		ps.setArray(index, ps.connection.createArrayOf("string", parameter.toTypedArray()))
	}
}

fun BaseTable<*>.list(name: String) = registerColumn(name, DatabaseList())