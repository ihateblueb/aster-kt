package site.remlit.blueb.plugin

import io.ktor.server.application.ApplicationCall
import org.pf4j.ExtensionPoint
import site.remlit.blueb.model.AuthenticationType

interface Route : ExtensionPoint {
	val authentication: AuthenticationType
	fun onCall(call: ApplicationCall)
}
