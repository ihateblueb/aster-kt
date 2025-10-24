package site.remlit.aster.exception

/**
 * Exception happening either with generating or running a database migration.
 *
 * @since 2025.9.1.1-SNAPSHOT
 * */
class MigrationException(message: String) : Exception(message)