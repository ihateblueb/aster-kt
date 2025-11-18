package site.remlit.aster.exception

/**
 * Exception to be thrown when an insert fails.
 * Usually, this is when an insert happens and a fetch returns null afterward.
 *
 * @since 2025.9.1.1-SNAPSHOT
 * */
class InsertFailureException(message: String) : Exception(message)
