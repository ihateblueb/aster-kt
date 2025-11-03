package site.remlit.aster.service

import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import site.remlit.aster.db.entity.KeyValEntity
import site.remlit.aster.db.table.KeyValTable
import site.remlit.aster.model.Service

/**
 * Service for persistently storing data for plugins and caching.
 *
 * @since 2025.10.5.0-SNAPSHOT
 * */
object KeyValService : Service {
	/**
	 * Get entity representation of a key/val.
	 *
	 * @param where Query to find entity
	 *
	 * @return Entity found, if any
	 * */
	fun getEntity(where: Op<Boolean>): KeyValEntity? = transaction {
		KeyValEntity
			.find { where }
			.singleOrNull()
	}

	/**
	 * Get entity representation of a key/val.
	 *
	 * @param key Key
	 *
	 * @return Entity found, if any
	 * */
	fun getEntity(key: String): KeyValEntity? = getEntity(KeyValTable.key eq key)

	/**
	 * Get value for a key
	 *
	 * @param where Query to find value
	 *
	 * @return Value
	 * */
	fun get(where: Op<Boolean>): String? = getEntity(where)?.value

	/**
	 * Get value for a key
	 *
	 * @param key Key
	 *
	 * @return Value
	 * */
	fun get(key: String): String? = get(KeyValTable.key eq key)

	/**
	 * Sets a key to a value.
	 * Will edit existing key, or create a new one.
	 *
	 * @param key Key
	 * @param value Value
	 * */
	fun set(key: String, value: String?) {
		val existing = getEntity(key)
		transaction {
			if (existing != null) {
				existing.value = value
				existing.flush()
			} else {
				KeyValEntity.new(IdentifierService.generate()) {
					this.key = key
					this.value = value
				}
			}
		}
	}

	/**
	 * Get value for a key
	 *
	 * @param where Query to find value
	 * */
	fun delete(where: Op<Boolean>) = getEntity(where)?.delete()

	/**
	 * Delete a key value pair
	 *
	 * @param key Key
	 * */
	fun delete(key: String) = getEntity(key)?.delete()
}