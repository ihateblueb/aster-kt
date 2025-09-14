package site.remlit.blueb.aster.exception

import io.ktor.http.*

/**
 * Exception happening with resolving a remote URL.
 *
 * @since 2025.7.1.0-SNAPSHOT
 * */
class ResolverException(val status: HttpStatusCode, message: String) : Exception(message)
