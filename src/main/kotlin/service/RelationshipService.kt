package site.remlit.blueb.aster.service

import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.dao.load
import site.remlit.blueb.aster.db.entity.RelationshipEntity
import site.remlit.blueb.aster.db.suspendTransaction
import site.remlit.blueb.aster.db.table.RelationshipTable
import site.remlit.blueb.aster.model.Relationship
import site.remlit.blueb.aster.model.RelationshipType
import site.remlit.blueb.aster.model.Service

/**
 * Service for managing user relationships.
 *
 * @since 2025.5.1.0-SNAPSHOT
 * */
class RelationshipService : Service() {
	companion object {
		/**
		 * Get a relationship.
		 *
		 * @param where Query to find relationship
		 *
		 * @return Found relationship, if any
		 * */
		suspend fun get(where: Op<Boolean>): Relationship? = suspendTransaction {
			val relationship = RelationshipEntity
				.find { where }
				.singleOrNull()
				?.load(RelationshipEntity::to, RelationshipEntity::from)


			if (relationship != null)
				Relationship.fromEntity(relationship)
			else null
		}

		/**
		 * Gets ID of activity used to create a relationship
		 *
		 * @param relationshipId ID of the relationship
		 *
		 * @return ActivityPub ID of the activity used to create the relationship
		 * */
		suspend fun getActivityId(relationshipId: String): String? = suspendTransaction {
			RelationshipEntity
				.find { RelationshipTable.id eq relationshipId }
				.singleOrNull()
				?.activityId
		}

		/**
		 * Get relationships in both directions for two users
		 *
		 * @param to Relationship target
		 * @param from Relationship owner
		 *
		 * @return [Pair] of [Relationship], where first is to and second is from
		 * */
		suspend fun getPair(to: String, from: String): Pair<Relationship?, Relationship?> {
			return Pair(
				this.get(RelationshipTable.to eq to and (RelationshipTable.from eq from)),
				this.get(RelationshipTable.to eq from and (RelationshipTable.from eq to))
			)
		}

		fun mapPair(pair: Pair<Relationship?, Relationship?>): List<Map<String, Relationship?>> {
			return listOf(
				mapOf(
					"to" to pair.first
				),
				mapOf(
					"from" to pair.second
				)
			)
		}

		/**
		 * Determine if there is a [RelationshipType.Block] in either direction
		 *
		 * @param to First user
		 * @param from Second user
		 *
		 * @return If either are blocking each other
		 * */
		suspend fun eitherBlocking(to: String, from: String): Boolean {
			val pair = this.getPair(to, from)

			if (pair.first != null && pair.first?.type == RelationshipType.Block)
				return true

			if (pair.second != null && pair.first?.type == RelationshipType.Block)
				return true

			return false
		}

		/**
		 * Determine if there is a [RelationshipType.Mute] in one direction
		 *
		 * @param to Relationship target
		 * @param from Relationship owner
		 *
		 * @return If `from` is muting `to`
		 * */
		suspend fun muteExists(to: String, from: String): Boolean {
			val relationship = this.get(RelationshipTable.to eq to and (RelationshipTable.from eq from))

			return relationship != null && relationship.type == RelationshipType.Mute
		}
	}
}
