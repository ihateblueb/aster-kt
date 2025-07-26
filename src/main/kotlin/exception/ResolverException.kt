package site.remlit.blueb.aster.exception

import io.ktor.http.*

class ResolverException(val status: HttpStatusCode, message: String) : Exception(message)
