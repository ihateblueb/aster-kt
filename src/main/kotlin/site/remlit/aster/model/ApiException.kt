package site.remlit.aster.model

import io.ktor.http.*

class ApiException(val status: HttpStatusCode, message: String = status.description) : Exception(message)
