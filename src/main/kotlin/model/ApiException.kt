package site.remlit.blueb.model

import io.ktor.http.*

class ApiException(val status: HttpStatusCode, message: String = status.description) : Exception(message)
