package site.remlit.blueb.aster.db

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.v1.core.Transaction
import org.jetbrains.exposed.v1.jdbc.transactions.experimental.newSuspendedTransaction

val Dispatchers.DB get() = Dispatchers.IO

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
	newSuspendedTransaction(Dispatchers.DB, statement = block)
