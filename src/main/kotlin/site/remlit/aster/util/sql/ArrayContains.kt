package site.remlit.aster.util.sql

import org.jetbrains.exposed.v1.core.ExpressionWithColumnType
import org.jetbrains.exposed.v1.core.arrayParam
import org.jetbrains.exposed.v1.core.dao.id.EntityID

/**
 * Determine if an array contains elements from another array.
 *
 * @param list Array of elements to check for
 *
 * @since 2025.10.5.0-SNAPSHOT
 * */
@JvmName("arrayContainsOpString")
infix fun <T> ExpressionWithColumnType<T>.arrayContains(list: List<String>) =
	ArrayContainsOp(this, arrayParam(list))

/**
 * Determine if an array contains elements from another array.
 *
 * @param list Array of elements to check for
 *
 * @since 2025.10.5.0-SNAPSHOT
 * */
@JvmName("arrayContainsOpEntityIDString")
infix fun <T> ExpressionWithColumnType<T>.arrayContains(list: List<EntityID<String>>) =
	ArrayContainsOp(this, arrayParam(list.map { it.toString() }))

/**
 * Determine if an array contains elements from another array.
 *
 * @param list Array of elements to check for
 *
 * @since 2025.10.5.0-SNAPSHOT
 * */
@JvmName("arrayContainsOpInt")
infix fun <T> ExpressionWithColumnType<T>.arrayContains(list: List<Int>) =
	ArrayContainsOp(this, arrayParam(list))