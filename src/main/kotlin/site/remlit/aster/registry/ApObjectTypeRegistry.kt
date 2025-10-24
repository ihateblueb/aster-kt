package site.remlit.aster.registry

import kotlinx.serialization.KSerializer
import org.jetbrains.annotations.ApiStatus
import site.remlit.aster.model.ap.ApActor
import site.remlit.aster.model.ap.ApNote
import site.remlit.aster.model.ap.ApObject
import site.remlit.aster.model.ap.ApTypedObject
import kotlin.reflect.KClass

object ApObjectTypeRegistry {
	val apObjectTypes =
		mutableListOf<Pair<KClass<out ApObject>, KSerializer<out ApObject>>>()

	/**
	 * If you have an ApObject that may be an `object` property on an activity, you need to register it here.
	 * */
	fun register(klass: KClass<out ApObject>, serializer: KSerializer<out ApObject>) {
		apObjectTypes.add(Pair(klass, serializer))
	}

	@ApiStatus.Internal
	fun registerInternal() {
		register(ApTypedObject::class, ApTypedObject.serializer())

		register(ApActor::class, ApActor.serializer())
		register(ApNote::class, ApNote.serializer())
	}
}