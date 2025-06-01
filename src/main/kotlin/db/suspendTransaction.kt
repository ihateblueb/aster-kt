package site.remlit.blueb.db

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

val Dispatchers.DB get() = Dispatchers.IO

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
	newSuspendedTransaction(Dispatchers.DB, statement = block)
