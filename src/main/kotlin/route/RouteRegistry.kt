package site.remlit.blueb.aster.route

import io.ktor.server.routing.*
import org.jetbrains.annotations.ApiStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import site.remlit.blueb.aster.model.Configuration
import site.remlit.blueb.aster.route.admin.AdminIndexRoutes
import site.remlit.blueb.aster.route.admin.AdminQueueRoutes
import site.remlit.blueb.aster.route.ap.ApNoteRoutes
import site.remlit.blueb.aster.route.ap.ApUserRoutes
import site.remlit.blueb.aster.route.ap.HostMetaRoutes
import site.remlit.blueb.aster.route.ap.InboxRoutes
import site.remlit.blueb.aster.route.ap.NodeInfoRoutes
import site.remlit.blueb.aster.route.ap.OutboxRoutes
import site.remlit.blueb.aster.route.ap.WebfingerRoutes
import site.remlit.blueb.aster.route.api.LoginRoutes
import site.remlit.blueb.aster.route.api.MetaRoutes
import site.remlit.blueb.aster.route.api.NoteRoutes
import site.remlit.blueb.aster.route.api.NotificationRoutes
import site.remlit.blueb.aster.route.api.RegisterRoutes
import site.remlit.blueb.aster.route.api.TimelineRoutes
import site.remlit.blueb.aster.route.api.UserRoutes
import site.remlit.blueb.aster.route.api.mod.InviteRoutes
import site.remlit.blueb.aster.route.api.mod.PolicyRoutes

/**
 * Registry for keeping track of built-in and plugin routes.
 *
 * @since 2025.10.1.0-SNAPSHOT
 */
object RouteRegistry {
	private val logger: Logger = LoggerFactory.getLogger(RouteRegistry::class.java)

	/**
	 * List of registered extension functions for routes.
	 */
	var routes: MutableList<Routing.() -> Unit> = emptyList<Routing.() -> Unit>().toMutableList()

	/**
	 * Register a route to be loaded by the server
	 *
	 * @param route Lambda containing route
	 */
	fun registerRoute(
		route: Routing.() -> Unit
	) {
		if (Configuration.debug) logger.debug("Registering route ${route::class.simpleName}")
		routes.add(route)
	}

	/**
	 * Internal method to install routes to the Ktor router
	 *
	 * @param routing Routing instance provided by Ktor
	 * */
	@ApiStatus.Internal
	fun installRoutes(routing: Routing) =
		routes.forEach { route ->
			routing.apply(route)
		}

	/**
	 * Registers Aster's built-in routes.
	 * */
	@ApiStatus.Internal
	fun registerBuiltinRoutes() {
		AdminIndexRoutes.register()
		AdminQueueRoutes.register()

		ApNoteRoutes.register()
		ApUserRoutes.register()
		HostMetaRoutes.register()
		InboxRoutes.register()
		NodeInfoRoutes.register()
		OutboxRoutes.register()
		WebfingerRoutes.register()

		InviteRoutes.register()
		PolicyRoutes.register()

		LoginRoutes.register()
		MetaRoutes.register()
		NoteRoutes.register()
		NotificationRoutes.register()
		RegisterRoutes.register()
		TimelineRoutes.register()
		UserRoutes.register()

		FrontendRoutes.register()
		UploadRoutes.register()
	}
}